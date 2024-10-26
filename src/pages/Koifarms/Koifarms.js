import React, { useEffect, useState } from "react";
import { NavLink } from "react-router-dom";
import ProductApi from "../../api/productApi";
const Troursers = () => {
  const [productList, setProductList] = useState([]);
  const [sortBy, setSortBy] = useState(""); // Khởi tạo state để lưu trữ sắp xếp sản phẩm theo giá

  const fetchProductList = async () => {
    try {
      const response = await ProductApi.getAll();
      setProductList(response);
      console.log(response);
    } catch (error) {
      console.log("fail", error);
    }
  };

  useEffect(() => {
    fetchProductList();
  }, []);

  // Hàm xử lý sự kiện cho button "Tăng dần"
  const handleSortAsc = () => {
    setSortBy("asc"); // Đặt state sắp xếp là "asc"
    setProductList((prevList) =>
      [...prevList].sort((a, b) => a.price - b.price)
    ); // Sắp xếp danh sách sản phẩm theo giá tăng dần
  };

  // Hàm xử lý sự kiện cho button "Giảm dần"
  const handleSortDesc = () => {
    setSortBy("desc"); // Đặt state sắp xếp là "desc"
    setProductList((prevList) =>
      [...prevList].sort((a, b) => b.price - a.price)
    ); // Sắp xếp danh sách sản phẩm theo giá giảm dần
  };

  return (
    <div>
      <div
        style={{
          backgroundColor: "#fff",
          padding: "20px",
          display: "flex",
          justifyContent: "end",
          marginTop: "45px",
          marginRight: "344px",
        }}
      >
        {/* Thêm 2 button để sắp xếp sản phẩm */}
        <div style={{ display: "flex", gap: "2rem" }}>
          <button className="buttonprice" onClick={handleSortAsc}>
            Giá: Tăng dần
          </button>

          <button className="buttonprice" onClick={handleSortDesc}>
            Giá: Giảm dần
          </button>
        </div>
      </div>


<div
        className="product-container"
      >
        {productList.slice(13, 25).map((product) => (
          <div key={product.id} className="col-span-1 w-h py-16 ml-6">
            <NavLink to={`/products/${product.id}`}>
              <div className="h-full  rounded-lg overflow-hidden boximg">
                <img
                  
                  // className="lg:h-48 md:h-36 w-full object-cover object-center transition duration-800 ease-out imgproduct"
                   className="lg:h-49 md:h-37 w-full object-contain object-center transition duration-800 ease-out"
                  src={product.imgUrl}
                  alt={product.name}
                />
                <div className="mt-5 box-2">
                  <h1 className="title-font text-lg  text-gray-900 mb-3 textdes">
                    {product.name}
                  </h1>
                  <p className="leading-relaxed mb-3 textprice text-red-600">
                    {product.price.toLocaleString("vi-VN")} ₫
                  </p>
                </div>
              </div>
            </NavLink>
          </div>
        ))}
      </div>
    </div>

  );
};
export default Troursers;
