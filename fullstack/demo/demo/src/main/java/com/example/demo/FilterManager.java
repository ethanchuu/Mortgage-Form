package com.example.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterManager {
    // A list to store all filters added by the user
    private final List<String> filters = new ArrayList<>();

    // Adds a new filter to the list
    public void addFilter(String filter) {
        filters.add(filter); // Add the given filter to the active list
    }

    // Deletes a specific filter from the list
    public void deleteFilter(String filter) {
        filters.removeAll(Collections.singleton(filter)); // Remove the filter that matches the given string
    }

    // Removes a filter by its index in the list
    public void removeFilter(int index) {
        if (index >= 0 && index < filters.size()) {
            filters.remove(index); // Remove the filter at the specified index
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
    }

    // Clears all filters from the list
    public void clearFilters() {
        filters.clear(); // Empty the list of all active filters
    }

    // Returns the current list of active filters
    public List<String> getFilters() {
        return filters; // Provide access to the filters
    }

    // Builds a SQL WHERE clause from the active filters
    public String buildWhereClause() {
        // If no filters exist, return "1=1" which means no filtering
        if (filters.isEmpty()) return "1=1";

        // Join all filters with "AND" to create a SQL-friendly WHERE clause
        return String.join(" AND ", filters);
    }
}
