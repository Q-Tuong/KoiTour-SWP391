// src/pages/Admin/AdminDashboard.js
import React from 'react';
import { Link } from 'react-router-dom';
import './styles1.css';
const AdminDashboard = () => {
  return (
    <div>
      <h1>Admin Dashboard</h1>
      <ul>
        <li>
          <Link to="/admin/users">Manage Users</Link>
        </li>
        <li>
          <Link to="/admin/tours">Manage Tours</Link>
        </li>
        <li>
          <Link to="/admin/kois">Manage Kois</Link>
        </li>
        <li>
          <Link to="/admin/koifarms">Manage Koi Farms</Link>
        </li>
        <li>
          <Link to="/admin/orders">Manage Orders</Link>
        </li>
      </ul>
    </div>
  );
};

export default AdminDashboard;
