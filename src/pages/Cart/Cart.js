import React, { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const Cart = () => {
  const [cartItems, setCartItems] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const fetchCartItems = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.get("http://14.225.212.120:8080/api/cart/get", {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      
      // Check if the response has items
      if (response.data.items) {
        setCartItems(response.data.items);
      } else {
        setCartItems([]);
      }
    } catch (err) {
      console.error("Error fetching cart items:", err);
      setError("Failed to fetch cart items. Please try again later.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCartItems();
  }, []);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (error) {
    return <div className="text-red-500">{error}</div>;
  }

  if (!cartItems.length) {
    return <div>No items in the cart.</div>;
  }

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Your Cart</h1>
        <table className="min-w-full border border-gray-300">
          <thead>
            <tr>
              <th className="border">Product ID</th>
              <th className="border">Product Name</th>
              <th className="border">Quantity</th>
              <th className="border">Price</th>
              <th className="border">Total</th>
            </tr>
          </thead>
          <tbody>
            {cartItems.map((item) => (
              <tr key={item.id}>
                <td className="border">{item.productId}</td>
                <td className="border">{item.productName}</td>
                <td className="border">{item.quantity}</td>
                <td className="border">{item.price.toLocaleString("vi-VN")} ₫</td>
                <td className="border">{(item.price * item.quantity).toLocaleString("vi-VN")} ₫</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      <Footer />
    </div>
  );
};

export default Cart;
