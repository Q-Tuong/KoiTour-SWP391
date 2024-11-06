import React, { useEffect, useState } from "react";
import axios from "axios";
import { NavLink, useNavigate } from "react-router-dom";
import "./bestseller.css";
import { BsFillArrowRightCircleFill } from "react-icons/bs";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const BestSeller = () => {
  const [tours, setTours] = useState([]); // State for tours
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1); // State for current page
  const [totalPages, setTotalPages] = useState(0); // State for total pages
  const navigate = useNavigate(); // To navigate on unauthorized access

  const pageSize = 5; // Define the size of each page

  useEffect(() => {
    const fetchTours = async () => {
      const token = localStorage.getItem("token"); // Retrieve token from localStorage

      if (!token) {
        navigate("/signin"); // Redirect to login if no token
        return;
      }

      try {
        const response = await axios.get(
          `http://14.225.212.120:8080/api/tour/get-all`,
          {
            params: {
              page: currentPage - 1, // API may expect page index starting from 0
              size: pageSize,
            },
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );

        console.log(response.data); // Log the full response to check its structure
        setTours(response.data.content || []); // Safeguard against undefined
        setTotalPages(response.data.totalPages || 0); // Safeguard against undefined
      } catch (error) {
        console.error("Error fetching tours:", error);
        if (error.response && error.response.status === 401) {
          navigate("/signin"); // Redirect to sign in on unauthorized access
        }
      } finally {
        setLoading(false); // Set loading to false after data is fetched
      }
    };

    fetchTours(); // Fetch tours based on the current page
  }, [currentPage, navigate]); // Include currentPage in the dependency array

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1); // Increment page number
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1); // Decrement page number
    }
  };

  if (loading) {
    return <div>Loading...</div>; // Loading state while fetching data
  }

  if (tours.length === 0) {
    return <div>No tours available.</div>; // Message for no data
  }

  return (
    <div>
      <Header />
      <div className="mt-28 bestseller">
        <h1 className="bestsellerh1">BestSeller</h1>
      </div>
      <div className="product-container">
        {tours.slice(0, 6).map((tour) => (
          <NavLink
            key={tour.id}
            to={`/tours/${tour.id}`}
            className="product-item"
          >
            <img
              className="transform hover:scale-110 transition"
              src={tour.imgUrl}
              alt={tour.name}
              style={{
                width: '100%',
                maxWidth: '300px',
                height: '300px',
                objectFit: 'cover',
                borderRadius: '8px',
                boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
              }}
              onError={(e) => {
                console.error('Image failed to load:', tour.imgUrl);
                e.target.src = 'https://via.placeholder.com/300';
              }}
            />
            <h3
              style={{ fontSize: "18px", marginTop: "40px" }}
              className="h3-textbestseller"
            >
              {tour.name}
            </h3>
            <p className="p-textdescription">{tour.description}</p>
            <p className="p-textprice">
              {tour.price.toLocaleString("vi-VN")} ₫
            </p>
          </NavLink>
        ))}
      </div>
      <div className="button-bottom mt-9">
        <NavLink to="/tours">
          <button className="mt-2 button text-sm fa fa-arrow-right mr-5 flex button-sell ">
            <p style={{ marginLeft: "2.4rem", fontSize: "22px" }}>ALL</p>
            <BsFillArrowRightCircleFill />
          </button>
        </NavLink>
      </div>
      <Footer />
    </div>
  );
};

export default BestSeller;
