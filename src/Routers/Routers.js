import React from "react";
import { Navigate, Route, Routes } from "react-router-dom";
import Home from "../pages/Home/Home";
import Cart from "../pages/Cart/Cart";
import BestSeller from "../pages/Best Seller/BestSeller";
import Kois from "../pages/Kois/Kois";
import KoiDetails from "../pages/Kois/KoiDetails";
// import CancelOrder from "../pages/Kois/CancelOrder";
// import CompleteOrder from "../pages/Kois/CompleteOrder";
// import GetAllKoiOrder from "../pages/Kois/GetAllKoiOrder";
// import GetPaidOrder from "../pages/Kois/GetPaidOrder";
// import OrderKoi from "../pages/Kois/OrderKoi";
// import RegeneratePayment from "../pages/Kois/RegeneratePayment";
// import SearchKoiOrderByEmail from "../pages/Kois/SearchKoiOrderByEmail";
// import TranKoi from "../pages/Kois/TranKoi";
import Koifarms from "../pages/Koifarms/Koifarms";
import Tours from "../pages/Tours/Tours"; 
import TourDetails from "../pages/Tours/ToursDetails";
// import TourCancelOrder from "../pages/Tours/CancelOrder"; // Renamed to avoid conflict
// import GetAllTourOrder from "../pages/Tours/getAllTourOrder";
// import GetPaidTourOrder from "../pages/Tours/GetPaidTourOrder";
// import TourRegeneratePayment from "../pages/Tours/RegeneratePayment"; // Renamed to avoid conflict
// import SearchTourOrderByEmail from "../pages/Tours/SearchTourOrderByEmail";
// import ViewTransactions from "../pages/Tours/ViewTransactions";
// import AddToCart from "../pages/Cart/AddToCart";
// import ClearCart from "../pages/Cart/ClearCart";
// import RemoveFromCart from "../pages/Cart/RemoveFromCart";
import Order from "../pages/Order/Order";
// import Transactions from "../pages/Order/Transactions";
import Pay from "../pages/pay/pay";
import Login from "../pages/Login/Logout";
import Signup from "../pages/Signup/Signup";
import Signin from "../pages/Signin/Signin";
import User from "../pages/User/User";
import ResetPass from "../pages/ResetPass/ResetPass";
import Dashboard from "../pages/Admin/Dashboard";
import UserList from "../pages/Admin/UserList";
import KoiList from "../pages/Admin/KoiList";
import TourList from "../pages/Admin/TourList";
import KoifarmList from "../pages/Admin/KoifarmList";
import OrderList from "../pages/Admin/OrderList";

export default function Routers() {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/home" replace />} />
      <Route path="/home" element={<Home />} />
      <Route path="/cart" element={<Cart />} />
      <Route path="/bestseller" element={<BestSeller />} />
      <Route path="/kois" element={<Kois />} />
      <Route path="/koifarms" element={<Koifarms />} />
      <Route path="/tours" element={<Tours />} />
      <Route path="/tours/:tourId" element={<TourDetails />} />
      <Route path="/kois/:koiId" element={<KoiDetails />} />
      <Route path="/pay" element={<Pay />} />
      <Route path="/login" element={<Login />} />
      <Route path="/order" element={<Order />} />
      {/* <Route path="/transactions" element={<Transactions />} /> */}
      <Route path="/signup" element={<Signup />} />
      <Route path="/signin" element={<Signin />} />
      <Route path="/user" element={<User />} />
      <Route path="/resetPass" element={<ResetPass />} />
      <Route path="/admin" element={<Dashboard />} />
      <Route path="/admin/kois" element={<KoiList />} />
      <Route path="/admin/users" element={<UserList />} />
      <Route path="/admin/tours" element={<TourList />} />
      <Route path="/admin/koifarms" element={<KoifarmList />} />
      <Route path="/admin/orders" element={<OrderList />} />
      {/* Tour-related routes */}
      {/* <Route path="/tours/cancel" element={<TourCancelOrder />} />
      <Route path="/tours/get-all-order" element={<GetAllTourOrder />} />
      <Route path="/tours/get-paid-order" element={<GetPaidTourOrder />} />
      <Route path="/tours/regenerate-payment" element={<TourRegeneratePayment />} />
      <Route path="/tours/search-order-by-email" element={<SearchTourOrderByEmail />} />
      <Route path="/tours/view-transactions" element={<ViewTransactions />} />
      {/* Koi-related routes */}
      {/* <Route path="/kois/cancel" element={<CancelOrder />} />
      <Route path="/kois/GetAllKoiOrder" element={<GetAllKoiOrder />} />
      <Route path="/kois/CompleteOrder" element={<CompleteOrder />} /> */}

      {/* <Route path="/kois/GetPaidOrder" element={<GetPaidOrder />} />
      <Route path="/kois/SearchKoiOrderByEmail" element={<RegeneratePayment />} />
      <Route path="/kois/SearchKoiOrderByEmail" element={<SearchKoiOrderByEmail />} />
      <Route path="/kois/TranKoi" element={<TranKoi />} /> */}
      {/* Cart-related routes */}
      {/* <Route path="/cart/AddToCart" element={<AddToCart />} />
      <Route path="/cart/ClearCart" element={<ClearCart />} />
      <Route path="/cart/RemoveFromCart" element={<RemoveFromCart />} /> } */}
    </Routes>
  );
}
