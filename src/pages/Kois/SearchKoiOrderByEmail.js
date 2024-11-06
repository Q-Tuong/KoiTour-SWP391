import React, { useState } from "react";
import axios from "axios";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "./SearchKoiOrderByEmail.css"; // Optional: Create a CSS file for styling

const SearchKoiOrderByEmail = () => {
  const [email, setEmail] = useState("");
  const [koiOrders, setKoiOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSearch = async () => {
    const token = localStorage.getItem("token");

    setLoading(true); // Set loading to true before fetching
    setError(""); // Reset error message

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/order/search-order-by-email/koi?email=${encodeURIComponent(email)}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setKoiOrders(response.data); // Set koi orders state
    } catch (err) {
      console.error("Error fetching koi orders by email:", err);
      setError("Failed to load koi orders."); // Set error message
    } finally {
      setLoading(false); // Set loading to false after fetching
    }
  };

  return (
    <div>
      <Header />
      <div className="search-koi-order-container">
        <h1>Search Koi Orders by Email</h1>
        <input
          type="email"
          placeholder="Enter customer email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="email-input"
        />
        <button onClick={handleSearch} className="search-button">
          Search
        </button>

        {loading && <p>Loading...</p>} {/* Show loading state */}
        {error && <p>{error}</p>} {/* Show error message */}
        {koiOrders.length > 0 ? (
          <table className="koi-order-table">
            <thead>
              <tr>
                <th>Order ID</th>
                <th>Customer ID</th>
                <th>Total</th>
                <th>Created At</th>
              </tr>
            </thead>
            <tbody>
              {koiOrders.map((order) => (
                <tr key={order.id}>
                  <td>{order.id}</td>
                  <td>{order.customer_id}</td>
                  <td>{order.total.toLocaleString("vi-VN")} ₫</td>
                  <td>{new Date(order.createAt).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <p>No koi orders found for this email.</p> // Handle case where no koi orders are found
        )}
      </div>
      <Footer />
    </div>
  );
};

export default SearchKoiOrderByEmail;
