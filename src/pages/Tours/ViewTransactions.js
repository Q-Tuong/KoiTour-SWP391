import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const ViewTransactions = () => {
  const [orderId, setOrderId] = useState("");
  const [loading, setLoading] = useState(false);
  const [transactions, setTransactions] = useState([]);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const fetchTransactions = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    setLoading(true);
    setError("");
    setTransactions([]);

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/order/transactions/tour`,
        {
          params: { orderId }, // Send the order ID as a query parameter
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setTransactions(response.data); // Assuming the response data is an array of transactions
    } catch (err) {
      console.error("Error fetching transactions:", err);
      setError("Failed to fetch transactions. Please check the Order ID.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">View Transactions for Tour Order</h1>
        <form onSubmit={fetchTransactions} className="mb-4">
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
            {loading ? "Loading..." : "Fetch Transactions"}
          </button>
        </form>

        {error && <div className="text-red-500">{error}</div>}
        
        {transactions.length > 0 && (
          <table className="min-w-full mt-4">
            <thead>
              <tr>
                <th className="border">Transaction ID</th>
                <th className="border">Order ID</th>
                <th className="border">Amount</th>
                <th className="border">Date</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((transaction, index) => (
                <tr key={index}>
                  <td className="border">{transaction.id}</td>
                  <td className="border">{transaction.orderId}</td>
                  <td className="border">{transaction.amount.toLocaleString("vi-VN")} ₫</td>
                  <td className="border">{new Date(transaction.date).toLocaleString()}</td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default ViewTransactions;
