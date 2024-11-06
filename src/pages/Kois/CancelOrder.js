import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const CancelOrder = () => {
  const [orderId, setOrderId] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleCancelOrder = async (e) => {
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
        `http://14.225.212.120:8080/api/order/update-to-cancel/koi`,
        { orderId }, // Send the order ID in the request body
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setMessage("Order canceled successfully!");
    } catch (err) {
      console.error("Error canceling order:", err);
      setError("Failed to cancel order. Please check the Order ID.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Cancel Koi Order</h1>
        <form onSubmit={handleCancelOrder} className="mb-4">
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
            className="bg-red-500 text-white px-4 py-2 rounded ml-2"
            disabled={loading}
          >
            {loading ? "Loading..." : "Cancel Order"}
          </button>
        </form>

        {message && <div className="text-green-500">{message}</div>}
        {error && <div className="text-red-500">{error}</div>}
      </div>
      <Footer />
    </div>
  );
};

export default CancelOrder;
