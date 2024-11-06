import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import axios from "axios";
import { v4 as uuidv4 } from "uuid"; // Importing uuid
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const TourDetails = () => {
  const { tourId } = useParams();
  const [tour, setTour] = useState(null);
  const [loading, setLoading] = useState(true);
  const [ordering, setOrdering] = useState(false);
  const [quantity, setQuantity] = useState(1);
  const navigate = useNavigate();

  const fetchTourDetails = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/tour/${tourId}/get-by-id`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setTour(response.data);
    } catch (error) {
      console.error("Error fetching tour details:", error);
      if (error.response && error.response.status === 401) {
        navigate("/signin");
      } else {
        alert("Failed to fetch tour details. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  const handleOrder = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }
  
    const customerId = localStorage.getItem("id");
    if (!customerId) {
      alert("Customer ID not found. Please sign in again.");
      navigate("/signin");
      return;
    }
  
    if (!tour || !tour.price) {
      alert("Tour details are not available. Cannot place order.");
      return;
    }
  
    const total = tour.price * parseInt(quantity, 10);
  
    const orderData = {
      customer_id: customerId,
      total: total,
      createAt: new Date().toISOString(),
      details: [
        {
          tour_id: tour.id, // Use the fetched tour id
          quantity: parseInt(quantity, 10), // Use the quantity entered by the user
          unitPrice: tour.price, // Use the price from the fetched tour
          totalPrice: total, // Calculate total price based on quantity and unit price
        },
      ],
    };
  
    setOrdering(true);
  
    try {
      const orderResponse = await axios.post(
        `http://14.225.212.120:8080/api/order/create/tour`,
        orderData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
  
      if (orderResponse.status === 200) {
        alert("Order placed successfully!");
        navigate("/orders");
      } else {
        alert("Failed to place order. Please try again later.");
      }
    } catch (error) {
      console.error("Error placing order:", error);
      if (error.response) {
        alert(
          `Failed to place order: ${
            error.response.data.message || "Please try again later."
          }`
        );
      } else {
        alert("Failed to place order. Please check your network connection.");
      }
    } finally {
      setOrdering(false);
    }
  };  

  useEffect(() => {
    fetchTourDetails();
  }, [tourId]);

  if (loading) {
    return <div>Loading...</div>;
  }

  if (!tour) {
    return <div>Tour not found.</div>;
  }

  return (
    <div>
      <Header />
      <div className="tour-details">
        <h1 className="text-2xl font-bold">{tour.name}</h1>
        <img
          src={tour.imgURL}
          alt={tour.name}
          className="mt-4 w-full h-auto object-cover"
        />
        <p className="mt-4 text-lg">{tour.description}</p>
        <p className="mt-4 text-lg text-red-600">
          Giá: {tour.price.toLocaleString("vi-VN")} ₫
        </p>

        <div className="mt-4">
          <label htmlFor="quantity" className="mr-2">
            Số lượng:
          </label>
          <input
            type="number"
            id="quantity"
            value={quantity}
            min="1"
            onChange={(e) =>
              setQuantity(Math.max(1, parseInt(e.target.value) || 1))
            }
            className="border p-1"
            disabled={loading || ordering}
          />
        </div>

        <button
          onClick={handleOrder}
          disabled={ordering}
          className={`mt-4 ${ordering ? "bg-gray-400" : "bg-blue-500"} text-white px-4 py-2 rounded`}
        >
          {ordering ? "Đang đặt hàng..." : "Đặt hàng"}
        </button>
      </div>
      <Footer />
    </div>
  );
};

export default TourDetails;
