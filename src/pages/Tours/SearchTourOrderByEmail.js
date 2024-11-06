import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const SearchTourOrderByEmail = () => {
  const [email, setEmail] = useState("");
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleSearch = async (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    setLoading(true);
    setError("");

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/order/search-order-by-email/tour`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            email: email, // Sending email as a query parameter
          },
        }
      );

      setOrders(response.data);
    } catch (err) {
      console.error("Error fetching orders by email:", err);
      setError("Không tìm thấy đơn hàng nào với email này.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Tìm kiếm đơn hàng theo email</h1>
        <form onSubmit={handleSearch} className="mb-4">
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Nhập email"
            required
            className="border rounded px-4 py-2"
          />
          <button
            type="submit"
            className="bg-blue-500 text-white px-4 py-2 rounded ml-2"
          >
            Tìm kiếm
          </button>
        </form>

        {loading && <div>Loading...</div>}
        {error && <div className="text-red-500">{error}</div>}
        {orders.length > 0 && (
          <table className="min-w-full border-collapse">
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
              {orders.map((order, index) => (
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
                      disabled={!order.details.length}
                    >
                      Xem chi tiết
                    </button>
                  </td>
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

export default SearchTourOrderByEmail;
