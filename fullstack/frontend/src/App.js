import React, { useState } from "react";
import "./App.css";

function App() {
  // State for filters and results
  const [filters, setFilters] = useState([]);
  const [results, setResults] = useState([]);
  const [loanAmountSum, setLoanAmountSum] = useState(0);

  // Add a new filter
  const addFilter = (filter) => {
    setFilters([...filters, filter]);
  };

  // Delete a filter
  const deleteFilter = (index) => {
    setFilters(filters.filter((_, i) => i !== index));
  };

  // Fetch results based on filters
  const fetchResults = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/mortgages", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ filters }),
      });
      const data = await response.json();
      setResults(data.mortgages);
      setLoanAmountSum(data.loanAmountSum);
    } catch (error) {
      console.error("Error fetching results:", error);
    }
  };

  return (
    <div className="App">
      <header className="App-header">
        <h1>Mortgage Search</h1>
      </header>

      <div className="filter-section">
        <h2>Add Filters</h2>
        <button onClick={() => addFilter("MSAMD = Example MSAMD")}>Add MSAMD</button>
        <button onClick={() => addFilter("Income-to-Debt Ratio > 0.5")}>
          Add Income-to-Debt Ratio
        </button>
        <button onClick={() => addFilter("County = Example County")}>
          Add County
        </button>
      </div>

      <div className="filters">
        <h2>Active Filters</h2>
        <ul>
          {filters.map((filter, index) => (
            <li key={index}>
              {filter} <button onClick={() => deleteFilter(index)}>Remove</button>
            </li>
          ))}
        </ul>
      </div>

      <div className="results-section">
        <h2>Results</h2>
        <button onClick={fetchResults}>Fetch Results</button>
        <p>Total Loans: {results.length}</p>
        <p>Total Loan Amount: ${loanAmountSum}</p>
        <ul>
          {results.map((result, index) => (
            <li key={index}>{result.name}</li>
          ))}
        </ul>
      </div>
    </div>
  );
}

export default App;
