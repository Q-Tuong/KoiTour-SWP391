import React, { useState } from "react";
import logologin from "../../asset/logo/LK.png";
import "./signin.css";
import { NavLink, useNavigate } from "react-router-dom";
import axios from "axios";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
function Signin() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage(""); // Clear previous messages
    setLoading(true); // Start loading

    try {
      const response = await axios.post(
        "http://14.225.212.120:8080/api/user/login",
        {
          username,
          password,
        }
      );

      const { token, fullname, id, role } = response.data; // Assuming customerId is returned

      // Securely store the token and user details
      localStorage.setItem("token", token);
      localStorage.setItem("fullname", fullname);
      localStorage.setItem("id", id); // Store customer ID

      // Navigate to different routes based on the role
      if (role === "ADMIN") {
        navigate("/Admin");
      } else if (role === "MANAGER") {
        navigate("/manager/dashboard");
      } else if (role === "CONSULTING_STAFF") {
        navigate("/consulting/dashboard");
      } else if (role === "SALE_STAFF") {
        navigate("/sale/dashboard");
      } else if (role === "CUSTOMER") {
        navigate("/Home");
      } else {
        setMessage("Unrecognized role");
      }
    } catch (error) {
      console.error("Login error:", error); // Log the error for debugging
      setLoading(false);
      if (error.response) {
        if (error.response.status === 401) {
          setMessage("Account or password not valid");
        } else {
          setMessage("Login failed. Please try again.");
        }
      } else {
        setMessage("Login failed. Please check your network connection.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <Header />
      <div className="login-container">
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="username" className="form-label">
              Tên đăng nhập:
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={(event) => {
                setUsername(event.target.value);
                setMessage(""); // Clear message when user types
              }}
              className="form-input"
            />
          </div>
          <div className="form-group">
            <label htmlFor="password" className="form-label">
              Mật khẩu:
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={(event) => {
                setPassword(event.target.value);
                setMessage(""); // Clear message when user types
              }}
              className="form-input"
            />
          </div>
          <span>
            <button type="submit" className="form-button" disabled={loading}>
              {loading ? "Loading..." : "SignIn"}
            </button>

            <NavLink to="/signup">
              <button className="form-button2 ml-3">SignUp</button>
            </NavLink>
            <div className="form-message">{message}</div>
          </span>

          <span>
            <p className="text-red-500">
              Quên mật khẩu ?{" "}
              <NavLink to="/resetPass">
                <button className="form-button3 ml-3">Reset Password</button>
              </NavLink>
            </p>
          </span>
        </form>
      </div>
      <Footer />
    </div>
  );
}

export default Signin;
