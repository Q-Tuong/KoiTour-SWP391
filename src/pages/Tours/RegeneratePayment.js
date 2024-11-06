import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const RegeneratePayment = () => {
  const [orderId, setOrderId] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRegeneratePayment = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    setLoading(true);
    setError("");
    setMessage("");

    try {
      const response = await axios.post(
        `http://14.225.212.120:8080/api/order/regenerate-payment/tour`,
        { orderId: orderId }, // Send the order ID in the request body
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setMessage("Payment has been regenerated successfully!");
    } catch (err) {
      console.error("Error regenerating payment:", err);
      setError("Failed to regenerate payment. Please check the Order ID.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Regenerate Payment for Tour Order</h1>
        <form onSubmit={handleRegeneratePayment} className="mb-4">
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
            className="bg-blue-500 text-white px-4 py-2 rounded ml-2"
            disabled={loading}
          >
            {loading ? "Processing..." : "Regenerate Payment"}
          </button>
        </form>

        {message && <div className="text-green-500">{message}</div>}
        {error && <div className="text-red-500">{error}</div>}
      </div>
      <Footer />
    </div>
  );
};

export default RegeneratePayment;
