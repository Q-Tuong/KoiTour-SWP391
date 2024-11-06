import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const RemoveFromCart = () => {
  const [koiId, setKoiId] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleRemoveFromCart = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.delete(
        `http://14.225.212.120:8080/api/cart/${koiId}/remove`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        alert("Item removed from cart successfully!");
        setKoiId(""); // Clear input after successful removal
      }
    } catch (err) {
      console.error("Error removing from cart:", err);
      setError("Failed to remove item from cart. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="container mx-auto p-4">
        <h1 className="text-2xl font-bold mb-4">Remove Item from Cart</h1>
        <form onSubmit={handleRemoveFromCart} className="mb-4">
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
          {error && <div className="text-red-500 mb-4">{error}</div>}
          <button
            type="submit"
            className={`bg-red-500 text-white px-4 py-2 rounded ${
              loading ? "opacity-50 cursor-not-allowed" : ""
            }`}
            disabled={loading}
          >
            {loading ? "Removing..." : "Remove from Cart"}
          </button>
        </form>
      </div>
      <Footer />
    </div>
  );
};

export default RemoveFromCart;
