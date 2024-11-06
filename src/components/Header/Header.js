import React, { useEffect, useState } from "react";
import "./header.css";
import { NavLink, useNavigate } from "react-router-dom";
import { BsCartPlusFill } from "react-icons/bs";
import { AiOutlineUser } from "react-icons/ai";
import logo from "../../asset/logo/logo1.png";

function Header() {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const navigate = useNavigate();

  // Check if token exists on mount
  useEffect(() => {
    const token = localStorage.getItem("token");
    setIsLoggedIn(!!token); // Set to true if token exists
  }, []);

  const handleLogout = () => {
    // Clear token and user details from localStorage
    localStorage.removeItem("token");
    localStorage.removeItem("fullname");
    localStorage.removeItem("id");
    setIsLoggedIn(false); // Update state
    navigate("/signin"); // Redirect to sign-in page
  };

  return (
    <header className="header">
      <div className="logo-container">
        <NavLink to="/home">
          <img src={logo} alt="logo" className="logo" style={{ width: '100px', height: 'auto' }} />
        </NavLink>
      </div>
      <nav className="nav">
        <ul className="nav-links">
          <li><NavLink className="nav" to="/kois">Kois</NavLink></li>
          <li><NavLink className="nav" to="/koifarms">Koifarms</NavLink></li>
          <li><NavLink className="nav" to="/tours">Tours</NavLink></li>
          <li><NavLink className="nav" to="/Order">Order</NavLink></li>
        </ul>
        {isLoggedIn ? (
          <div className="user-menu-container">
            <AiOutlineUser size={20} />
            <div className="user-menu">
              <NavLink className="text-slate-950" to="/user">
                Thông tin người dùng
              </NavLink>
              <button className="text-slate-950" onClick={handleLogout}>
                Đăng xuất
              </button>
            </div>
          </div>
        ) : (
          <div className="items-center flex-shrink-0 hidden lg:flex sign">
            <NavLink to="/signin">
              <button className="self-center px-8 py-3 rounded text-white hover:text-green-200 transition-colors duration-300">
                Sign in
              </button>
            </NavLink>
            <NavLink to="/signup">
              <button className="self-center px-8 py-3 rounded text-white hover:text-green-200 transition-colors duration-300">
                Sign up
              </button>
            </NavLink>
          </div>
        )}
        <div className="items-center flex-shrink-0 hidden lg:flex cart-font ml-5">
          <NavLink to="/cart">
            <button style={{ backgroundColor: "transparent", border: "none" }}>
              <BsCartPlusFill style={{ color: "#fff", fontSize: "15px" }} />
            </button>
          </NavLink>
        </div>
      </nav>
    </header>
  );
}

export default Header;
