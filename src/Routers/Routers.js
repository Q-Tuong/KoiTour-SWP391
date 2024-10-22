import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import Home from "../pages/Home/Home";
import Signin from "../pages/Signin/Signin";
import ResetPass from "../pages/ResetPass/ResetPass";
import Signup from "../pages/Signup/Signup";
import Kois from "../pages/Kois/Kois"

export default function Routers() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/home" replace />} />
      <Route path="/home" element={<Home />} />
      <Route path="/signin" element={<Signin />} />
      <Route path="/resetPass" element={<ResetPass />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/kois" element={<Kois/>} />
    </Routes>
  );
}
