# Mortgage Backed Securities Application

## Completed Project Components
- [x] Backend Command Line Interface
- [x] Mortgage Filtering
- [x] Rate Calculation
- [x] Mortgage Securitization
- [x] Extra Credit: All 7 Filter Types
- [x] Extra Credit: Serializable Transactions
- [x] Extra Credit: Add New Mortgage
- [x] Frontend Application

### Extra Credit Attempted and Completed
- Implemented all 7 filter types (MSAMD, Income-to-debt ratio, County, Loan Type, Tract-to-MSAMD Income, Loan Purpose, Property Type)
- Added Serializable Transaction handling
- Developed functionality to add new mortgages
- Created a full-featured web frontend

## Known Issues
The backend application experienced difficulties connecting to the PostgreSQL database. This stemmed from:

Incorrect Database Credentials: The username, password, database URL, or port configured in the application properties did not match the database settings.
JDBC Driver Issues: The PostgreSQL JDBC driver was not working and configuring with the part 1 database

There were inconsistencies between the data types used in the backend code and the actual database schema. Examples of this issue could include:

Mapping a VARCHAR column to an Integer in the code.
Using a DATE type in the database but handling it as a String in the backend.
Precision mismatches in DECIMAL or FLOAT fields.

CORS Restrictions: The backend server did not allow requests from the frontend’s origin.
API Endpoint Mismatches: The frontend did not react to the routes and endpoints
Server Not Running: The backend server did properly start or reachable at the expected URL.

## Collaboration and Resources
### Collaborators
- Discussed database design with study group members in CS Database class
- Consulted with Professor during office hours about transaction handling

### Resources Consulted
- Spring Boot official documentation
- PostgreSQL JDBC documentation
- React.js tutorials on official React website
- ChatGPT 4o for frontend development guidance
- Stack Overflow for specific technical challenges
- Maven dependency management guides

## Development Challenges
- Implementing complex database filtering logic
- Ensuring transaction integrity during securitization process
- Synchronizing backend logic with frontend components
- Handling various edge cases in mortgage data processing
- Learning and integrating Spring Boot with React for frontend

## Time Spent on Project
- Approximate total time: 45 hours
  - Backend development: 20 hours
  - Frontend development: 15 hours
  - Debugging and testing: 10 hours

## Project Setup and Running Instructions
### Prerequisites
- Java 17 or higher
- PostgreSQL 13+
- Maven 3.8+
- Node.js 14+
- React

### Backend Setup
1. Create PostgreSQL database
2. Configure database connection in `application.properties`
3. Run Maven commands to build project
