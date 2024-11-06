// src/pages/Admin/AdminDashboard.js
import React from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";
import "./styles1.css";

const AdminDashboard = () => {
  const navigate = useNavigate();

  const handleLogout = () => {
    // Clear user data from local storage
    localStorage.removeItem("token");
    localStorage.removeItem("fullname");
    localStorage.removeItem("id");
    // Redirect to login page
    navigate("/login");
  };

  return (
    <div>
      <h1>Admin Dashboard</h1>
      <ul className="Dashboard">
        <li>
          <Link to="/admin/users" className="manage-link">
            Manage Users
          </Link>
        </li>
        <li>
          <Link to="/admin/tours" className="manage-link">
            Manage Tours
          </Link>
        </li>
        <li>
          <Link to="/admin/kois" className="manage-link">
            Manage Kois
          </Link>
        </li>
        <li>
          <Link to="/admin/koifarms" className="manage-link">
            Manage Koi Farms
          </Link>
        </li>
        <li>
          <Link to="/admin/orders" className="manage-link">
            Manage Orders
          </Link>
        </li>
        <span>
          <button onClick={handleLogout} className="form-button2 logout-button">Logout</button>
        </span>
      </ul>
    </div>
  );
};

export default AdminDashboard;
