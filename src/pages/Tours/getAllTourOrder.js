import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const GetAllTourOrder = () => {
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
    return <div className="text-center">Loading...</div>; // Loading state
  }

  if (!orders.length) {
    return <div className="text-center">Không có đơn hàng nào được tìm thấy.</div>; // More user-friendly message
  }

  return (
    <div>
      <Header />
      <div className="orders">
        <h1 className="text-2xl font-bold">Danh sách đơn hàng</h1>
        <table className="min-w-full mt-4 border-collapse">
          <thead>
            <tr>
              <th className="border px-4 py-2">Tour ID</th>
              <th className="border px-4 py-2">Số lượng</th>
              <th className="border px-4 py-2">Giá</th>
              <th className="border px-4 py-2">Trạng thái</th>
              <th className="border px-4 py-2">Hoạt động</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order, index) => {
              // Check if order.details exists and has elements
              const orderDetails = order.details && order.details.length > 0 ? order.details[0] : null;

              return (
                <tr key={index}>
                  <td className="border px-4 py-2">{orderDetails ? orderDetails.tourId : "N/A"}</td>
                  <td className="border px-4 py-2">{orderDetails ? orderDetails.quantity : "N/A"}</td>
                  <td className="border px-4 py-2">
                    {orderDetails ? orderDetails.totalPrice.toLocaleString("vi-VN") : "N/A"} ₫
                  </td>
                  <td className="border px-4 py-2">Đang chờ</td>
                  <td className="border px-4 py-2">
                    <button
                      onClick={() => navigate(`/transactions/${orderDetails ? orderDetails.tourId : ""}`)}
                      className="bg-yellow-500 text-white px-2 py-1 rounded"
                      disabled={!orderDetails} // Disable if order details are not available
                    >
                      Hoàn tất giao dịch
                    </button>
                  </td>
                </tr>
              );
            })}
          </tbody>
        </table>
      </div>
      <Footer />
    </div>
  );
};

export default GetAllTourOrder;
