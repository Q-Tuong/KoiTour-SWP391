import React, { useEffect, useState } from "react";
import "./kois.css";
import { NavLink } from "react-router-dom";
import axios from "axios";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

const Kois = () => {
  const [koi, setKoi] = useState([]);
  const [sortedKoi, setSortKoi] = useState([]);
  const [pageNumber, setPageNumber] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(true);
  const [sortAsc, setSortAsc] = useState(true);

  const fetchKoi = async (page) => {
    const token = localStorage.getItem("token");

    setLoading(true);

    try {
      const response = await axios.get(
        "http://14.225.212.120:8080/api/koi/get-all",
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
          params: {
            page: page - 1,
          },
        }
      );

      const data = response.data;
      setKoi(data.content);
      setSortKoi(data.content);
      setTotalPages(data.totalPages);
    } catch (err) {
      console.error("Error fetching products:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchKoi(pageNumber);
  }, [pageNumber]);

  const handleSortAsc = () => {
    const sorted = [...koi].sort((a, b) => a.price - b.price);
    setSortKoi(sorted);
    setSortAsc(true);
  };

  const handleSortDesc = () => {
    const sorted = [...koi].sort((a, b) => b.price - a.price);
    setSortKoi(sorted);
    setSortAsc(false);
  };

  return (
    <div>
      <Header />
      <div className="product-container">
        {loading ? (
          <p className="loading">Loading...</p>
        ) : sortedKoi.length === 0 ? (
          <p className="loading">No koi available.</p>
        ) : (
          sortedKoi.slice(0, 12).map((product) => (
            <div key={product.id} className="col-span-1 w-h py-16 ml-6">
              <NavLink to={`/kois/${product.id}`}>
                <div className="h-full rounded-lg overflow-hidden boximg">
                  <div className="img-container">
                    <img
                      src={product.imgUrl}
                      alt={product.name}
                    />
                  </div>
                  <div className="mt-5 box-2">
                    <h1 className="title-font text-lg text-gray-900 mb-3 textdes">
                      {product.name}
                    </h1>
                    <p className="leading-relaxed mb-3 textprice text-red-600">
                      {product.price.toLocaleString("vi-VN")} ₫
                    </p>
                    <button 
                      className="btn btn-success" 
                      style={{
                        textAlign: 'center',
                        display: 'flex',
                        justifyContent: 'center',
                        alignItems: 'center',
                        width: '100%'
                      }}
                    >
                      Xem chi tiết
                    </button>
                  </div>
                </div>
              </NavLink>
            </div>
          ))
        )}
      </div>
      <br />
      <div className="pagination-controls">
        <button
          disabled={pageNumber <= 1}
          onClick={() => setPageNumber((prev) => Math.max(prev - 1, 1))}
        >
          Previous
        </button>
        <span>
          Page {pageNumber} of {totalPages}
        </span>
        <button
          disabled={pageNumber >= totalPages}
          onClick={() => setPageNumber((prev) => Math.min(prev + 1, totalPages))}
        >
          Next
        </button>
      </div>
      <Footer />
    </div>
  );
};

export default Kois;
