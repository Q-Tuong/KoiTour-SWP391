import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import "./product.css";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
const Products = () => {
  const { koiId } = useParams();
  const [koi, setKoi] = useState(null);
  const [quantity, setQuantity] = useState(() => {
    // Retrieve quantity from session storage if available, default to 1
    const savedQuantity = sessionStorage.getItem(`koi-quantity-${koiId}`);
    return savedQuantity ? parseInt(savedQuantity) : 1;
  });
  const [loading, setLoading] = useState(true); // Loading state
  const [error, setError] = useState(null); // Error state

  const fetchKoiDetail = async () => {
    const token = localStorage.getItem("token");

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/koi/get-by-id/${koiId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setKoi(response.data);
    } catch (err) {
      console.error("Error fetching koi details:", err);
      setError("Không thể tải thông tin cá koi. Vui lòng thử lại sau."); // Error message
    } finally {
      setLoading(false); // Update loading state to false after data is fetched
    }
  };

  useEffect(() => {
    fetchKoiDetail();
  }, [koiId]);

  const handleQuantityChange = (change) => {
    const newQuantity = Math.max(1, quantity + change);
    setQuantity(newQuantity);
    sessionStorage.setItem(`koi-quantity-${koiId}`, newQuantity); // Save quantity in session storage
  };

  const handleAddToCart = async () => {
    const token = localStorage.getItem("token");
    const totalPrice = quantity * koi.price; // Calculate total price

    try {
      const response = await axios.post(
        "http://14.225.212.120:8080/api/order/create",
        {
          koiId,
          quantity,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      console.log("Order created:", response.data);
      alert(`Added ${quantity} of ${koi.name} to cart. Total price: ${totalPrice.toLocaleString("vi-VN")} ₫`);
    } catch (error) {
      console.error("Error creating order:", error);
      alert("Không thể thêm đơn hàng. Vui lòng thử lại sau.");
    }
  };

  if (loading) return <p>Loading...</p>; // Show loading state

  if (error) return <p>{error}</p>; // Show error message

  if (!koi) return <p>No koi found.</p>; // Fallback for no koi data

  return (
    <div><Header />
    <div className="koi-detail">
      <h1>{koi.name}</h1>
      <img src={koi.image} alt={koi.name} />
      <p>Price: {koi.price.toLocaleString("vi-VN")} ₫</p>
      <div>
        <button onClick={() => handleQuantityChange(-1)}>-</button>
        <span>{quantity}</span>
        <button onClick={() => handleQuantityChange(1)}>+</button>
      </div>
      <button onClick={handleAddToCart}>Thêm vào giỏ hàng</button>
    </div>
    <Footer />
    </div>
  );
};

export default Products;
