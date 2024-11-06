import React, { useEffect, useState } from "react";
import axios from "axios";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "./GetAllKoiOrder.css"; // Optional: Create a CSS file for styling

const GetAllKoiOrder = () => {
  const [koiOrders, setKoiOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchAllKoiOrders = async () => {
    const token = localStorage.getItem("token");

    setLoading(true); // Set loading to true before fetching

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/order/get-all/koi`, // Endpoint to fetch all koi orders
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setKoiOrders(response.data); // Set koi orders state
    } catch (err) {
      console.error("Error fetching all koi orders:", err);
      setError("Failed to load koi orders."); // Set error message
    } finally {
      setLoading(false); // Set loading to false after fetching
    }
  };

  useEffect(() => {
    fetchAllKoiOrders();
  }, []);

  if (loading) {
    return <p>Loading...</p>; // Show loading state
  }

  if (error) {
    return <p>{error}</p>; // Show error message
  }

  if (koiOrders.length === 0) {
    return <p>No koi orders found.</p>; // Handle case where no koi orders are found
  }

  return (
    <div>
      <Header />
      <div className="get-all-koi-order-container">
        <h1>All Koi Orders</h1>
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
      </div>
      <Footer />
    </div>
  );
};

export default GetAllKoiOrder;
