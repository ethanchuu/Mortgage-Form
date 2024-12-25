package com.example.demo;

import java.sql.*;
import java.util.Scanner;

public class MortgageCLI {
    private static final FilterManager filterManager = new FilterManager();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayActiveFiltersWithStats();

            System.out.println("\nOptions:");
            System.out.println("1. Add Filter");
            System.out.println("2. Delete Filter");
            System.out.println("3. Calculate Rate");
            System.out.println("4. Add New Mortgage");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1 -> addFilter(scanner);
                case 2 -> deleteFilter(scanner);
                case 3 -> calculateRateWithTransaction();
                case 4 -> addNewMortgage(scanner);
                case 5 -> {
                    System.out.println("Exiting...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void displayActiveFiltersWithStats() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            String whereClause = filterManager.buildWhereClause();
            String query = """
                SELECT COUNT(*) AS RowCount, SUM(CAST(loan_amount AS NUMERIC)) AS TotalAmount 
                FROM Application 
                WHERE %s
            """.formatted(whereClause.isEmpty() ? "1=1" : whereClause);

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                System.out.println("\nActive Filters:");
                filterManager.getFilters().forEach(System.out::println);

                if (rs.next()) {
                    int rowCount = rs.getInt("RowCount");
                    double totalAmount = rs.getDouble("TotalAmount");
                    System.out.println("Matching Rows: " + rowCount + ", Total Loan Amount: " + totalAmount);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving statistics: " + e.getMessage());
        }
    }

    private static void addFilter(Scanner scanner) {
        System.out.println("Choose a filter type:");
        System.out.println("1. MSAMD");
        System.out.println("2. Income-to-Debt Ratio");
        System.out.println("3. County");
        System.out.println("4. Loan Type");
        System.out.println("5. Tract-to-MSAMD Income");
        System.out.println("6. Loan Purpose");
        System.out.println("7. Property Type");
        System.out.println("8. Owner Occupied");

        int filterType = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        String filter = null;

        try {
            switch (filterType) {
                case 1 -> {
                    System.out.println("Enter MSAMD:");
                    String msamd = scanner.nextLine();
                    filter = "msamd = '" + msamd + "'";
                }
                case 2 -> {
                    System.out.println("Enter minimum Income/Loan Ratio:");
                    double min = scanner.nextDouble();
                    System.out.println("Enter maximum Income/Loan Ratio:");
                    double max = scanner.nextDouble();
                    filter = "applicant_income / loan_amount BETWEEN " + min + " AND " + max;
                }
                case 3 -> {
                    System.out.println("Enter County:");
                    String county = scanner.nextLine();
                    filter = "county_code = '" + county + "'";
                }
                case 4 -> {
                    System.out.println("Enter Loan Type:");
                    String loanType = scanner.nextLine();
                    filter = "loan_type_code = '" + loanType + "'";
                }
                case 5 -> {
                    System.out.println("Enter minimum Tract-to-MSAMD Income:");
                    double tractMin = scanner.nextDouble();
                    System.out.println("Enter maximum Tract-to-MSAMD Income:");
                    double tractMax = scanner.nextDouble();
                    filter = "tract_to_msamd_income BETWEEN " + tractMin + " AND " + tractMax;
                }
                case 6 -> {
                    System.out.println("Enter Loan Purpose:");
                    String purpose = scanner.nextLine();
                    filter = "loan_purpose = '" + purpose + "'";
                }
                case 7 -> {
                    System.out.println("Enter Property Type:");
                    String propertyType = scanner.nextLine();
                    filter = "property_type_code = '" + propertyType + "'";
                }
                case 8 -> {
                    System.out.println("Owner Occupied? (true/false):");
                    boolean ownerOccupied = scanner.nextBoolean();
                    filter = "approval_status = " + ownerOccupied;
                }
                default -> System.out.println("Invalid filter type.");
            }
        } catch (Exception e) {
            System.out.println("Error processing filter: " + e.getMessage());
        }

        if (filter != null) {
            filterManager.addFilter(filter);
            System.out.println("Filter added.");
        }
    }

    private static void calculateRateWithTransaction() {
        try (Connection conn = DatabaseUtil.getConnection()) {
            conn.setAutoCommit(false);

            String whereClause = filterManager.buildWhereClause();
            String query = """
                SELECT CAST(a.loan_amount AS NUMERIC) AS loan_amount, 
                       COALESCE(p.rate_spread, 0) AS rate_spread, 
                       CAST(p.lien_status AS INTEGER) AS lien_status
                FROM Application a 
                JOIN preliminary p ON a.applicant_id = p.application_id
                WHERE %s
            """.formatted(whereClause.isEmpty() ? "1=1" : whereClause);

            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                double totalAmount = 0, weightedSum = 0;

                while (rs.next()) {
                    double loanAmount = rs.getDouble("loan_amount");
                    double rateSpread = rs.getDouble("rate_spread");
                    int lienStatus = rs.getInt("lien_status");

                    if (rateSpread == 0) {
                        rateSpread = lienStatus == 1 ? 2.33 + 1.5 : 2.33 + 3.5;
                    }

                    totalAmount += loanAmount;
                    weightedSum += loanAmount * rateSpread;
                }

                if (totalAmount > 0) {
                    double weightedRate = weightedSum / totalAmount;
                    System.out.println("Weighted Rate: " + weightedRate);
                    System.out.println("Total Loan Amount: " + totalAmount);

                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Do you accept the rate? (yes/no)");
                    String response = scanner.nextLine();

                    if ("yes".equalsIgnoreCase(response)) {
                        String updateQuery = "UPDATE Application SET purchaser_type = 'Private Securitization' WHERE %s"
                                .formatted(whereClause.isEmpty() ? "1=1" : whereClause);
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                            int rowsUpdated = updateStmt.executeUpdate();
                            conn.commit();
                            System.out.println(rowsUpdated + " rows updated. Exiting...");
                        }
                    } else {
                        conn.rollback();
                    }
                } else {
                    System.out.println("No matching loans found.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Transaction failed: " + e.getMessage());
        }
    }

    private static void addNewMortgage(Scanner scanner) {
        try (Connection conn = DatabaseUtil.getConnection()) {
            System.out.println("Enter Applicant Income:");
            double income = scanner.nextDouble();
            System.out.println("Enter Loan Amount:");
            double loanAmount = scanner.nextDouble();
            scanner.nextLine(); // Consume newline
            System.out.println("Enter MSAMD:");
            String msamd = scanner.nextLine();
            System.out.println("Enter Loan Type:");
            String loanType = scanner.nextLine();

            String locationQuery = "SELECT location_id FROM Location WHERE msamd = ? LIMIT 1";
            try (PreparedStatement locationStmt = conn.prepareStatement(locationQuery)) {
                locationStmt.setString(1, msamd);
                try (ResultSet locationRs = locationStmt.executeQuery()) {
                    int locationId = locationRs.next() ? locationRs.getInt("location_id") : 0;

                    String insertQuery = """
                        INSERT INTO Application 
                        (applicant_income, loan_amount, location_id, loan_type_code)
                        VALUES (?, ?, ?, ?)
                    """;
                    try (PreparedStatement stmt = conn.prepareStatement(insertQuery)) {
                        stmt.setDouble(1, income);
                        stmt.setDouble(2, loanAmount);
                        stmt.setInt(3, locationId);
                        stmt.setString(4, loanType);

                        stmt.executeUpdate();
                        System.out.println("New mortgage added.");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error adding mortgage: " + e.getMessage());
        }
    }

    private static void deleteFilter(Scanner scanner) {
        System.out.println("Current Filters:");
        for (int i = 0; i < filterManager.getFilters().size(); i++) {
            System.out.println((i + 1) + ": " + filterManager.getFilters().get(i));
        }

        System.out.println("Enter filter number to delete:");
        int index = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        try {
            filterManager.removeFilter(index - 1);
            System.out.println("Filter removed.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Invalid filter number.");
        }
    }
}
