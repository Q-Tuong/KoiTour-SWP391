import { useLocation, useNavigate } from "react-router-dom";
import { useState, useRef } from "react";
import "./PaymentPage.css"; // import CSS file
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

export default function PaymentPage() {
  const location = useLocation();
  const cart = location.state.cart;
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [address, setAddress] = useState("");
  const formRef = useRef(null);

  const totalPrice = cart.reduce(
    (acc, item) => acc + item.price * item.quantity,
    0
  );

  const handleSubmit = (e) => {
    e.preventDefault();
    // Gửi thông tin thanh toán tới server ở đây
    console.log("Email:", email);
    console.log("Phone:", phone);
    console.log("Address:", address);
    console.log("Cart:", cart);
    console.log("Total Price:", totalPrice);
    // Chuyển hướng về trang chủ sau khi thanh toán thành công
    navigate("/");
  };

  return (
    <div><Header />
    <div className="payment-page-container">
      <h2 className="payment-page-title">Payment Page</h2>
      <ul className="payment-page-cart">
        {cart.map((item) => (
          <li className="payment-page-cart-item" key={item.id}>
            <img
              src={item.imgUrl}
              alt={item.name}
              className="payment-page-cart-item-img"
            />
            <div className="payment-page-cart-item-info">
              <h3 className="payment-page-cart-item-name">{item.name}</h3>
              <p className="payment-page-cart-item-price">
                Price: {item.price}
              </p>
              <p className="payment-page-cart-item-quantity">
                Quantity: {item.quantity}
              </p>
            </div>
          </li>
        ))}
      </ul>

      <p className="payment-page-total-price">Total Price: {totalPrice}</p>
      <form ref={formRef} onSubmit={handleSubmit} className="payment-page-form">
        <label className="payment-page-form-label">
          Email:
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
            className="payment-page-form-input"
          />
        </label>
        <br />
        <label className="payment-page-form-label">
          Phone:
          <input
            type="tel"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            required
            className="payment-page-form-input"
          />
        </label>
        <br />
        <label className="payment-page-form-label">
          Address:
          <textarea
            value={address}
            onChange={(e) => setAddress(e.target.value)}
            required
            className="payment-page-form-textarea"
          />
        </label>
        <br />
        <button type="submit" className="payment-page-form-button">
          Pay now
        </button>
      </form>
    </div>
    <Footer />
    </div>
  );
}
