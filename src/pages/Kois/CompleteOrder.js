import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams, useHistory } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "./CompleteOrder.css"; // Optional: Create a CSS file for styling

const CompleteOrder = () => {
  const { orderId } = useParams(); // Get orderId from the URL
  const [orderDetails, setOrderDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const history = useHistory(); // For navigation after updating

  const fetchOrderDetails = async () => {
    const token = localStorage.getItem("token");

    setLoading(true); // Set loading to true before fetching

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/order/transactions/koi`, // Assuming this endpoint fetches order details
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            orderId: orderId, // Send orderId as a query parameter
          },
        }
      );

      setOrderDetails(response.data); // Set order details state
    } catch (err) {
      console.error("Error fetching order details:", err);
      setError("Failed to load order details."); // Set error message
    } finally {
      setLoading(false); // Set loading to false after fetching
    }
  };

  const completeOrder = async () => {
    const token = localStorage.getItem("token");

    try {
      await axios.post(
        `http://14.225.212.120:8080/api/order/update-to-complete/koi`, // Endpoint to update order status
        { orderId: orderId }, // Send orderId in the body
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Navigate to the transaction details page after completion
      history.push(`/tranKoi/${orderId}`); // Adjust the route as necessary
    } catch (err) {
      console.error("Error updating order to complete:", err);
      setError("Failed to update order to complete."); // Set error message
    }
  };

  useEffect(() => {
    fetchOrderDetails();
  }, [orderId]);

  if (loading) {
    return <p>Loading...</p>; // Show loading state
  }

  if (error) {
    return <p>{error}</p>; // Show error message
  }

  if (!orderDetails) {
    return <p>No order details found.</p>; // Handle case where order details are not found
  }

  return (
    <div>
      <Header />
      <div className="complete-order-container">
        <h1>Complete Order</h1>
        <p><strong>Order ID:</strong> {orderDetails.id}</p>
        <p><strong>Total Price:</strong> {orderDetails.totalPrice.toLocaleString("vi-VN")} $</p>
        <p><strong>Status:</strong> {orderDetails.status}</p>
        <button onClick={completeOrder} className="complete-order-button">
          Mark as Complete
        </button>
        {/* Add more details as needed */}
      </div>
      <Footer />
    </div>
  );
};

export default CompleteOrder;
