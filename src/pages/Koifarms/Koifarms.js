import axios from "axios";
import { useState, useEffect } from "react";
import { NavLink } from "react-router-dom";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
import "../Kois/kois.css";

const Koifarms = () => {
  const [koiFarms, setKoiFarms] = useState([]);
  const [pageNumber, setPageNumber] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [sortedFarms, setSortedFarms] = useState([]);
  const [isSortedAsc, setIsSortedAsc] = useState(true); // Track sorting order

  const fetchKoiFarms = async (page) => {
    const token = localStorage.getItem("token"); // Get the token

    try {
      const response = await axios.get(
        `http://14.225.212.120:8080/api/koiFarm/get-all`,
        {
          headers: {
            Authorization: `Bearer ${token}`, // Add token to Authorization header
          },
          params: {
            page: page - 1, // API pages start from 0, UI from 1
          },
        }
      );

      const data = response.data;
      setKoiFarms(data.content);
      setSortedFarms(data.content); // Initialize sortedFarms with the fetched data
      setTotalPages(data.totalPages);
    } catch (error) {
      console.error("Error fetching koi farms:", error);
    }
  };

  useEffect(() => {
    fetchKoiFarms(pageNumber); // Call fetch function on mount or page change
  }, [pageNumber]);

  const handleSortAsc = () => {
    const sorted = [...koiFarms].sort((a, b) => a.price - b.price);
    setSortedFarms(sorted);
    setIsSortedAsc(true); // Update sorting state
  };

  const handleSortDesc = () => {
    const sorted = [...koiFarms].sort((a, b) => b.price - a.price);
    setSortedFarms(sorted);
    setIsSortedAsc(false); // Update sorting state
  };

  return (
    <div>
      <Header />
      
      <div>
        {sortedFarms.length === 0 ? (
          <p>No koi farms available.</p>
        ) : (
          <div className="product-container">
            {sortedFarms.slice(0, 12).map((farm) => (
              <div key={farm.id} className="col-span-1 w-h py-16 ml-6">
                <NavLink to={`/farms/${farm.id}`}>
                  <div className="h-full rounded-lg overflow-hidden boximg">
                    <img
                      className="farm-image"
                      src={farm.imgUrl}
                      alt={farm.name}
                      onError={(e) => {
                        console.error('Image failed to load:', farm.imgUrl);
                        e.target.src = 'https://via.placeholder.com/300';
                      }}
                    />
                    <div className="mt-5 box-2">
                      <h1 className="title-font text-lg text-gray-900 mb-3 textdes">
                        {farm.name}
                      </h1>
                      <p className="leading-relaxed mb-3 textprice text-red-600">
                        {farm.description}
                      </p>
                    </div>
                  </div>
                </NavLink>
              </div>
            ))}
          </div>
        )}
      </div>

      {/* Pagination Controls */}
      <div className="pagination-controls">
        <button
          disabled={pageNumber <= 1}
          onClick={() => setPageNumber((prev) => prev - 1)}
        >
          Previous
        </button>
        <span>
          Page {pageNumber} of {totalPages}
        </span>
        <button
          disabled={pageNumber >= totalPages}
          onClick={() => setPageNumber((prev) => prev + 1)}
        >
          Next
        </button>
      </div>
      <Footer />
    </div>
  );
};

export default Koifarms;
