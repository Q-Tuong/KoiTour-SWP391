import React, { useEffect, useState } from "react";
import axios from "axios";
import { useParams } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "./KoiDetails.css"; // You can create a separate CSS file for styling

const KoiDetails = () => {
  const { koiId } = useParams(); // Get koiId from the URL
  const [koiDetails, setKoiDetails] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [quantity, setQuantity] = useState(0); // State for quantity
  const [orderSuccess, setOrderSuccess] = useState("");

  const fetchKoiDetails = async () => {
    const token = localStorage.getItem("token");
    
    setLoading(true); // Set loading to true before fetching

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/koi/${koiId}/get-by-id`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setKoiDetails(response.data); // Set koi details state
    } catch (err) {
      console.error("Error fetching koi details:", err);
      setError("Failed to load koi details."); // Set error message
    } finally {
      setLoading(false); // Set loading to false after fetching
    }
  };

  // Function to handle order submission
  const handleOrder = async () => {
    const token = localStorage.getItem("token");
    const orderData = {
      totalPrice: koiDetails.price * quantity,
      details: [
        {
          koiId: koiDetails.id, // Ensure you're using the correct ID here
          quantity: quantity,
        },
      ],
    };

    try {
      const response = await axios.post(
        "http://14.225.212.120:8080/api/order/create/koi",
        orderData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );
      setOrderSuccess("Order placed successfully!"); // Show success message
      setQuantity(0); // Reset quantity after successful order
    } catch (err) {
      console.error("Error placing order:", err);
      setOrderSuccess("Failed to place order."); // Show error message
    }
  };

  useEffect(() => {
    fetchKoiDetails();
  }, [koiId]);

  if (loading) {
    return <p>Loading...</p>; // Show loading state
  }

  if (error) {
    return <p>{error}</p>; // Show error message
  }

  if (!koiDetails) {
    return <p>No details found for this koi.</p>; // Handle case where koi details are not found
  }

  return (
    <div>
      <Header />
      <div className="koi-details-container">
        <h1 className="koi-title">{koiDetails.name}</h1>
        <img
          className="koi-image"
          src={koiDetails.imgUrl}
          alt={koiDetails.name}
        />
        <p className="koi-price">
          Price: {koiDetails.price.toLocaleString("vi-VN")} ₫
        </p>
        <p className="koi-description">{koiDetails.description}</p>
        
        {/* Quantity Input */}
        <div>
          <label htmlFor="quantity">Quantity:</label>
          <input
            type="number"
            id="quantity"
            min="1"
            value={quantity}
            onChange={(e) => setQuantity(Number(e.target.value))}
          />
        </div>
        
        {/* Order Button */}
        <button onClick={handleOrder} className="btn btn-primary">
          Place Order
        </button>
        
        {/* Order Success/Error Message */}
        {orderSuccess && <p>{orderSuccess}</p>}
      </div>
      <Footer />
    </div>
  );
};

export default KoiDetails;
