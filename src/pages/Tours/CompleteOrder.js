import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const CompleteOrder = () => {
  const [orderId, setOrderId] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleCompleteOrder = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    setLoading(true);
    setMessage("");
    setError("");

    try {
      const response = await axios.post(
        `http://14.225.212.120:8080/api/order/update-to-complete/tour`,
        { orderId }, // Send the order ID in the request body
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setMessage("Order marked as complete successfully!");
    } catch (err) {
      console.error("Error completing order:", err);
      setError("Failed to mark order as complete. Please check the Order ID.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Complete Tour Order</h1>
        <form onSubmit={handleCompleteOrder} className="mb-4">
          <input
            type="text"
            value={orderId}
            onChange={(e) => setOrderId(e.target.value)}
            placeholder="Enter Order ID"
            required
            className="border rounded px-4 py-2"
          />
          <button
            type="submit"
            className="bg-green-500 text-white px-4 py-2 rounded ml-2"
            disabled={loading}
          >
            {loading ? "Loading..." : "Complete Order"}
          </button>
        </form>

        {message && <div className="text-green-500">{message}</div>}
        {error && <div className="text-red-500">{error}</div>}
      </div>
      <Footer />
    </div>
  );
};

export default CompleteOrder;
