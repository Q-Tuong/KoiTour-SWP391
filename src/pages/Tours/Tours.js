import React, { useEffect, useState } from "react";
import { NavLink, useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "../Kois/kois.css";

const Tours = () => {
  const [tours, setTours] = useState([]);
  const [loading, setLoading] = useState(true);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const navigate = useNavigate();

  const pageSize = 10;

  const fetchTours = async () => {
    const token = localStorage.getItem("token");
    if (!token) {
      navigate("/signin");
      return;
    }

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/tour/get-all`,
        {
          params: {
            page: currentPage - 1,
            size: pageSize,
          },
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      setTours(response.data.content || []);
      setTotalPages(response.data.totalPages || 0);
    } catch (error) {
      console.error("Error fetching tours:", error);
      if (error.response && error.response.status === 401) {
        navigate("/signin");
      } else {
        alert("Failed to fetch tours. Please try again later.");
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchTours();
  }, [currentPage]);

  const handleNextPage = () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
    }
  };

  const handlePreviousPage = () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
    }
  };

  if (loading) {
    return <div>Loading...</div>;
  }

  if (tours.length === 0) {
    return <div>No tours available.</div>;
  }

  const handleSortAsc = () => {
    const sortedTours = [...tours].sort((a, b) => a.price - b.price);
    setTours(sortedTours);
  };

  const handleSortDesc = () => {
    const sortedTours = [...tours].sort((a, b) => b.price - a.price);
    setTours(sortedTours);
  };

  return (
    <div>
      <Header />

      <div className="product-container">
        {tours.map((tour) => (
          <div key={tour.id} className="col-span-1 w-h py-16 ml-6">
            <NavLink to={`/tours/${tour.id}`}>
              <div className="h-full rounded-lg overflow-hidden boximg">
                <img
                  className="lg:h-49 md:h-37 w-full object-contain object-center transition duration-800 ease-out"
                  src={tour.imgUrl}
                  alt={tour.name}
                />
                <div className="mt-5 box-2">
                  <h1 className="title-font text-lg text-gray-900 mb-3 textdes">
                    {tour.name}
                  </h1>
                  <p className="leading-relaxed mb-3 textprice text-red-600">
                    {tour.price.toLocaleString("vi-VN")} ₫
                  </p>
                  <NavLink to={`/tours/${tour.id}`} className="button-details">
                    Xem chi tiết
                  </NavLink>
                </div>
              </div>
            </NavLink>
          </div>
        ))}
      </div>
      
      <div className="pagination">
        <button onClick={handlePreviousPage} disabled={currentPage === 1}>
          Previous
        </button>
        <span>{`Page ${currentPage} of ${totalPages}`}</span>
        <button onClick={handleNextPage} disabled={currentPage === totalPages}>
          Next
        </button>
      </div>
      
      <Footer />
    </div>
  );
};

export default Tours;
