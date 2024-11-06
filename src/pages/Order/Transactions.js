// src/pages/Transaction.js
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const Transaction = () => {
  const { tourId } = useParams();
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const handleCompleteTransaction = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.post(
        `http://14.225.212.120:8080/api/order/transactions/tour`,
        { tourId }, // Pass the tourId to the backend
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );

      alert("Transaction completed successfully!");
      navigate("/orders"); // Redirect to orders page after completing transaction
    } catch (error) {
      console.error("Error completing transaction:", error);
      alert("Failed to complete the transaction. Please try again later.");
    }
  };

  useEffect(() => {
    // You can fetch additional details if needed here
    setLoading(false); // Set loading to false immediately for this demo
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <Header />
      <div className="transaction">
        <h1 className="text-2xl font-bold">Hoàn tất giao dịch cho Tour ID: {tourId}</h1>
        <button
          onClick={handleCompleteTransaction}
          className="mt-4 bg-green-500 text-white px-4 py-2 rounded"
        >
          Xác nhận giao dịch
        </button>
      </div>
      <Footer />
    </div>
  );
};

export default Transaction;
