import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "./TranKoi.css"; // Optional: Create a CSS file for styling

const TranKoi = () => {
  const { orderId } = useParams(); // Get orderId from the URL
  const [transactionDetails, setTransactionDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchTransactionDetails = async () => {
    const token = localStorage.getItem("token");

    setLoading(true); // Set loading to true before fetching

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/order/transactions/koi`, // Keep the orderId out of the URL
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            orderId: orderId, // Send orderId as a query parameter
          },
        }
      );

      setTransactionDetails(response.data); // Set transaction details state
    } catch (err) {
      console.error("Error fetching transaction details:", err);
      setError("Failed to load transaction details."); // Set error message
    } finally {
      setLoading(false); // Set loading to false after fetching
    }
  };

  useEffect(() => {
    fetchTransactionDetails();
  }, [orderId]);

  if (loading) {
    return <p>Loading...</p>; // Show loading state
  }

  if (error) {
    return <p>{error}</p>; // Show error message
  }

  if (!transactionDetails) {
    return <p>No transaction details found.</p>; // Handle case where transaction details are not found
  }

  return (
    <div>
      <Header />
      <div className="tran-koi-container">
        <h1>Transaction Details</h1>
        <p><strong>Order ID:</strong> {transactionDetails.id}</p>
        <p><strong>Total Price:</strong> {transactionDetails.totalPrice.toLocaleString("vi-VN")} ₫</p>
        <p><strong>Quantity:</strong> {transactionDetails.details.map(detail => (
          <span key={detail.koiId}>{detail.quantity} {detail.koiId}</span>
        ))}</p>
        <p><strong>Status:</strong> {transactionDetails.status}</p>
        {/* Add more details as needed */}
      </div>
      <Footer />
    </div>
  );
};

export default TranKoi;
