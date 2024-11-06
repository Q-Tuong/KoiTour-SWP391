import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const GetPaidTourOrder = () => {
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
        "http://14.225.212.120:8080/api/order/get-paid-order/tour",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setOrders(response.data); // Set the fetched orders directly
    } catch (error) {
      console.error("Error fetching paid orders:", error);
      alert("Failed to fetch paid orders. Please try again later.");
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
    return <div className="text-center">Không có đơn hàng nào được thanh toán.</div>; // No orders message
  }

  return (
    <div>
      <Header />
      <div className="orders">
        <h1 className="text-2xl font-bold">Danh sách đơn hàng đã thanh toán</h1>
        <table className="min-w-full mt-4 border-collapse">
          <thead>
            <tr>
              <th className="border px-4 py-2">Tour ID</th>
              <th className="border px-4 py-2">Số lượng</th>
              <th className="border px-4 py-2">Giá</th>
              <th className="border px-4 py-2">Trạng thái</th>
              <th className="border px-4 py-2">Ngày tạo</th>
              <th className="border px-4 py-2">Hoạt động</th>
            </tr>
          </thead>
          <tbody>
            {orders.map((order, index) => {
              // Assume each order has a details property containing an array of order details
              return (
                <tr key={index}>
                  <td className="border px-4 py-2">{order.details[0]?.tour_id || "N/A"}</td>
                  <td className="border px-4 py-2">{order.details[0]?.quantity || "N/A"}</td>
                  <td className="border px-4 py-2">{order.total.toLocaleString("vi-VN")} ₫</td>
                  <td className="border px-4 py-2">Đã thanh toán</td>
                  <td className="border px-4 py-2">{new Date(order.createAt).toLocaleDateString("vi-VN")}</td>
                  <td className="border px-4 py-2">
                    <button
                      onClick={() => navigate(`/transactions/${order.details[0]?.tour_id || ""}`)}
                      className="bg-yellow-500 text-white px-2 py-1 rounded"
                      disabled={!order.details.length} // Disable if no order details are available
                    >
                      Xem chi tiết
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

export default GetPaidTourOrder;
