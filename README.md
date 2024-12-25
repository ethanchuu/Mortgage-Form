
# Mortgage Form Project

A full-stack application that allows users to search for and manage mortgages, including filtering, rate calculations, and packaging mortgages into Mortgage-Backed Securities (MBS). Built with a **Spring Boot backend** and a **React frontend**, this project enables data-driven mortgage analysis and operations.

---

## Features

### Backend (Spring Boot):
- **Mortgage Data Management**:
  - REST APIs for filtering, fetching, and managing mortgage data.
  - Integration with a PostgreSQL database for persistent storage.
- **Filter and Search**:
  - Filter mortgages based on:
    - MSAMD (Metropolitan Statistical Area/Metropolitan Division).
    - Income-to-Debt Ratio.
    - County.
    - Loan Type.
    - Loan Purpose.
    - Tract-to-MSAMD-Income.
    - Property Type.
- **Rate Calculation**:
  - Computes weighted average rates for Mortgage-Backed Securities based on loan amounts.

### Frontend (React):
- **User-Friendly UI**:
  - Add, manage, and delete filters dynamically.
  - Display search results with total loan amounts.
  - Interactive interface for mortgage analysis.
- **Live Data Fetching**:
  - Integrates seamlessly with backend REST APIs.
  - Displays real-time mortgage data.

---

<img width="1512" alt="Screenshot 2024-12-25 at 12 19 04 AM" src="https://github.com/user-attachments/assets/ebbae328-e4e1-4e01-aafa-3b6a37ecac54" />

---


## Tech Stack

### Backend:
- **Spring Boot**: RESTful API development.
- **PostgreSQL**: Relational database for mortgage data.
- **Java**: Backend programming.

### Frontend:
- **React**: Frontend framework for building interactive UIs.
- **Bootstrap**: Styling and responsive design.
- **JavaScript**: Client-side scripting.

---

## Project Structure

```
Mortgage-Form/
├── fullstack/
│   ├── demo/                   # Backend (Spring Boot)
│   │   ├── src/                # Java source files
│   │   ├── pom.xml             # Maven build file
│   │   ├── application.properties
│   ├── frontend/               # Frontend (React)
│   │   ├── src/
│   │   ├── public/
│   │   ├── package.json
│   │   ├── README.md
```

---

## How to Run

### Backend (Spring Boot):
1. Navigate to the backend folder:
   ```bash
   cd fullstack/demo
   ```
2. Start the backend:
   ```bash
   mvn spring-boot:run
   ```
3. Backend will run on:
   ```
   http://localhost:8080
   ```

### Frontend (React):
1. Navigate to the frontend folder:
   ```bash
   cd fullstack/frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the frontend:
   ```bash
   npm start
   ```
4. Open your browser and go to:
   ```
   http://localhost:3000
   ```

---

## Features Implemented

- Filters for Mortgage Search:
  - MSAMD (Metropolitan Statistical Area/Metropolitan Division).
  - Income-to-Debt Ratio (minimum and/or maximum).
  - County.
  - Loan Type.
  - Tract-to-MSAMD-Income (minimum and/or maximum).
  - Loan Purpose.
  - Property Type.

- Display Results:
  - Display a list of mortgages matching the filters.
  - Show the total number of results and the sum of loan amounts.

- Filter Management:
  - Add filters.
  - Delete individual filters or reset all filters.

- Rate Calculation:
  - Allow calculation of the expected rate using a weighted average based on the loan amount.

---

## Author

**Ethan Chu**  

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---
