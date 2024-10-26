import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import Home from "../pages/Home/Home";
import Signin from "../pages/Signin/Signin";
import ResetPass from "../pages/ResetPass/ResetPass";
import Signup from "../pages/Signup/Signup";
import Kois from "../pages/Kois/Kois"
import User from "../pages/User/User";
import Tours from "../pages/Tours/Tours";
import Koifarms from "../pages/Koifarms/Koifarms";
import Cart from "../pages/Cart/Cart";

export default function Routers() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/home" replace />} />
      <Route path="/home" element={<Home />} />
      <Route path="/signin" element={<Signin />} />
      <Route path="/resetPass" element={<ResetPass />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/kois" element={<Kois/>} />
      <Route path="/user" element={<User />} />
      <Route path="/tours" element={<Tours/>} />
      <Route path="/cart" element={<Cart />} />
      <Route path="/koifarms" element={<Koifarms />} />
    </Routes>
  );
}
