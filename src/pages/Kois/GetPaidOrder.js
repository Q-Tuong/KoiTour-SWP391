import React, { useEffect, useState } from "react";
import axios from "axios";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "./GetPaidOrder.css"; // Optional: Create a CSS file for styling

const GetPaidOrder = () => {
  const [paidOrders, setPaidOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const fetchPaidOrders = async () => {
    const token = localStorage.getItem("token");

    setLoading(true); // Set loading to true before fetching

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/order/get-paid-order/koi`, // Endpoint to fetch paid orders
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setPaidOrders(response.data); // Set paid orders state
    } catch (err) {
      console.error("Error fetching paid orders:", err);
      setError("Failed to load paid orders."); // Set error message
    } finally {
      setLoading(false); // Set loading to false after fetching
    }
  };

  useEffect(() => {
    fetchPaidOrders();
  }, []);

  if (loading) {
    return <p>Loading...</p>; // Show loading state
  }

  if (error) {
    return <p>{error}</p>; // Show error message
  }

  if (paidOrders.length === 0) {
    return <p>No paid orders found.</p>; // Handle case where no paid orders are found
  }

  return (
    <div>
      <Header />
      <div className="get-paid-order-container">
        <h1>Paid Orders</h1>
        <ul className="paid-orders-list">
          {paidOrders.map((order) => (
            <li key={order.id} className="paid-order-item">
              <h2>Order ID: {order.id}</h2>
              <p><strong>Status:</strong> {order.status}</p>
              <p><strong>Total:</strong> {order.total.toLocaleString("vi-VN")} ₫</p>
              <p><strong>Customer Email:</strong> {order.customerEmail}</p>
              <p><strong>Created At:</strong> {new Date(order.createAt).toLocaleString()}</p>
              <h3>Customer Details:</h3>
              <p><strong>Name:</strong> {order.customer.firstName} {order.customer.lastName}</p>
              <p><strong>Phone:</strong> {order.customer.phone}</p>
              <p><strong>Address:</strong> {order.customer.address}</p>
              <p><strong>Koi Balance:</strong> {order.customer.koiBalance.toLocaleString("vi-VN")} ₫</p>
            </li>
          ))}
        </ul>
      </div>
      <Footer />
    </div>
  );
};

export default GetPaidOrder;
