import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  const fetchOrders = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.get(
        "http://14.225.212.120:8080/api/order/get-all/tour",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      // Get any new order from sessionStorage
      const newOrder = sessionStorage.getItem("orderDetails");
      if (newOrder) {
        setOrders((prevOrders) => [...prevOrders, JSON.parse(newOrder)]);
        sessionStorage.removeItem("orderDetails"); // Clear the session after using
      }

      setOrders((prevOrders) => [...prevOrders, ...response.data]); // Append the fetched orders
    } catch (error) {
      console.error("Error fetching orders:", error);
      alert("Failed to fetch orders. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!orders.length) {
    return <div>No orders found.</div>;
  }

  return (
    <div>
      <Header />
      <div className="orders">
        <h1 className="text-2xl font-bold">Danh sách đơn hàng</h1>
        <table className="min-w-full mt-4">
          <thead>
            <tr>
              <th className="border">Tour ID</th>
              <th className="border">Số lượng</th>
              <th className="border">Giá</th>
              <th className="border">Trạng thái</th>
              <th className="border">Hoạt động</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order, index) => (
              <tr key={index}>
                <td className="border">{order.details[0].tourId}</td>
                <td className="border">{order.details[0].quantity}</td>
                <td className="border">{order.details[0].totalPrice.toLocaleString("vi-VN")} ₫</td>
                <td className="border">Đang chờ</td>
                <td className="border">
                  <button
                    onClick={() => navigate(`/transactions/${order.details[0].tourId}`)}
                    className="bg-yellow-500 text-white px-2 py-1 rounded"
                  >
                    Hoàn tất giao dịch
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Footer />
    </div>
  );
};

export default Orders;
