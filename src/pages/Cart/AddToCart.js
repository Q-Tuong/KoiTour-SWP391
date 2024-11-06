import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const AddToCart = () => {
  const [koiId, setKoiId] = useState("");
  const [quantity, setQuantity] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleAddToCart = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.post(
        `http://14.225.212.120:8080/api/cart/${koiId}/add`,
        { quantity },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        alert("Item added to cart successfully!");
        // Optionally redirect or clear fields
        setKoiId("");
        setQuantity(1);
      }
    } catch (err) {
      console.error("Error adding to cart:", err);
      setError("Failed to add item to cart. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Add Item to Cart</h1>
        <form onSubmit={handleAddToCart} className="mb-4">
          <div className="mb-4">
            <label htmlFor="koiId" className="block mb-2">
              Koi ID:
            </label>
            <input
              type="text"
              id="koiId"
              value={koiId}
              onChange={(e) => setKoiId(e.target.value)}
              required
              className="border rounded p-2 w-full"
            />
          </div>
          <div className="mb-4">
            <label htmlFor="quantity" className="block mb-2">
              Quantity:
            </label>
            <input
              type="number"
              id="quantity"
              value={quantity}
              onChange={(e) => setQuantity(e.target.value)}
              min="1"
              required
              className="border rounded p-2 w-full"
            />
          </div>
          {error && <div className="text-red-500 mb-4">{error}</div>}
          <button
            type="submit"
            className={`bg-blue-500 text-white px-4 py-2 rounded ${
              loading ? "opacity-50 cursor-not-allowed" : ""
            }`}
            disabled={loading}
          >
            {loading ? "Adding..." : "Add to Cart"}
          </button>
        </form>
      </div>
      <Footer />
    </div>
  );
};

export default AddToCart;
