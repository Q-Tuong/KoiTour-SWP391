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
    console.log('Fetching koi details...');
    console.log('KoiId:', koiId);

    const token = localStorage.getItem("token");
    console.log('Token exists:', !!token);

    setLoading(true);
    setError("");

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/koi/get-by-id/${koiId}`,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      ).catch(function (error) {
        console.log(error.toJSON());
      });
      console.log('API Response:', response.data);
      setKoiDetails(response.data);
    } catch (err) {
      console.log('Error details:', {
        message: err.message,
        response: err.response?.data,
        status: err.response?.status
      });
      if (err.response?.data === 'Expired token!') {
        setError("Your session has expired. Please log in again.");
        localStorage.removeItem("token");
      } else {
        setError("Failed to load koi details. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  // Function to handle order submission
  const handleOrder = async () => {
    const token = localStorage.getItem("token");
    const kid = koiDetails?.id ?? koiId;

    setError(""); // Reset error state

    if (!kid || quantity <= 0) {
      setError("Please select a valid quantity");
      return;
    }

    try {
      const response = await axios.post(
        `http://14.225.212.120:8080/api/cart/add/${kid}`,
        {
          koiId: kid,
          // quantity: quantity
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          }
        }
      );

      if (response.data) {
        setOrderSuccess("Item added to cart successfully!");
        // Optionally reset quantity
        setQuantity(1);
      }
    } catch (err) {
      console.error("Error adding to cart:", err);
      if (err.response?.data.includes('Expired token')) {
        setError("Your session has expired. Please log in again.");
        localStorage.removeItem("token");
      } else if (err.response?.data) {
        setError(err.response.data);
      } else {
        setError("Failed to add item to cart. Please try again later.");
      }
    }
  };

  useEffect(() => {
    fetchKoiDetails();
  }, [koiId]);

  useEffect(() => {
    if (koiDetails) {
      console.log('=== KOI DETAILS ===');
      console.log('ID:', koiDetails?.id);
      console.log('Name:', koiDetails?.name);
      console.log('Type:', koiDetails?.type);
      console.log('Size:', koiDetails?.size);
      console.log('Origin:', koiDetails?.origin);
      console.log('Price:', koiDetails?.price);
      console.log('Description:', koiDetails?.description);
      console.log('Image URL:', koiDetails?.imgUrl);
      console.log('==================');
    } else {
      console.log('koiDetails is null or undefined');
    }
  }, [koiDetails]);

  if (loading) {
    return <p>Loading...</p>; // Show loading state
  }

  if (error) {
    // return <p>{error}</p>; // Show error message
  }

  if (!koiDetails) {
    // return <p>No details found for this koi.</p>; // Handle case where koi details are not found
  }

  return (
    <div>
      <Header />
      {koiDetails ? (
        <>
          <div className="koi-details-container">
            {error && <p className="error-message">{error}</p>}
            <h1 className="koi-title">{koiDetails.name}</h1>
            <img
              className="koi-image"
              src={koiDetails.imgUrl}
              alt={koiDetails.name}
            />

            {/* Thêm thông tin chi tiết */}
            <div className="koi-info-grid">
              <div className="info-item">
                <span className="info-label">Type:</span>
                <span className="info-value">{koiDetails.type}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Size:</span>
                <span className="info-value">{koiDetails.size}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Origin:</span>
                <span className="info-value">{koiDetails.origin}</span>
              </div>
              <div className="info-item">
                <span className="info-label">Price:</span>
                <span className="info-value price">
                  {koiDetails.price.toLocaleString("vi-VN")} ₫
                </span>
              </div>
            </div>

            <p className="koi-description">{koiDetails.description}</p>

            {/* Quantity Input */}
            <div>
              <label htmlFor="quantity">Quantity:</label>
              <input
                type="number"
                id="quantity"
                min="1"
                value={quantity}
                onChange={(e) => setQuantity(Math.max(1, Number(e.target.value)))}
              />
            </div>

            {/* Order Button */}
            <button onClick={handleOrder} className="btn btn-primary">
              Add to Cart
            </button>
          

          {/* Order Success/Error Message */}
          {orderSuccess && <p className="success-message">{orderSuccess}</p>}
        </div>
        </>
          ) : (
          <>No data found</>
      )}

      <Footer />
    </div>
  );
};

export default KoiDetails;
