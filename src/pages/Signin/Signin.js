import React, { useState } from "react";
import { userApi } from "../../api/userApi"; // Make sure this is still necessary
import logologin from "../../asset/logo/LK.png"; // If you're using this logo, consider adding it to the form
import "./signin.css";
import { NavLink, useNavigate } from "react-router-dom";
import axios from "axios";

function Signin() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false); // New loading state
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage(''); // Clear previous messages
    setLoading(true); // Start loading

    try {
      const response = await axios.post('http://14.225.212.120:8080/api/user/login', { // Ensure this is the correct endpoint
        username,
        password
      });

      const { token, fullname } = response.data;

      // Securely store the token
      localStorage.setItem("token", token);
      localStorage.setItem("fullname", fullname);
      navigate('/Home');
    } catch (error) {
      setLoading(false); // Stop loading
      if (error.response) {
        // Handle specific HTTP errors
        if (error.response.status === 401) {
          setMessage('Account or password not valid');
        } else {
          setMessage('Login failed. Please try again.');
        }
      } else {
        setMessage('Login failed. Please check your network connection.');
      }
    } finally {
      setLoading(false); // Ensure loading stops in case of error
    }
  };

  return (
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
              setMessage(''); // Clear message when user types
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
              setMessage(''); // Clear message when user types
            }}
            className="form-input"
          />
        </div>
        <span><button type="submit" className="form-button" disabled={loading}>
          {loading ? 'Loading...' : 'SignIn'}
        </button>
       
        <NavLink to="/signup">
              <button className="form-button2 ml-3">SignUp</button>
            </NavLink>
        <div className="form-message">{message}</div></span>
        
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
  );
}

export default Signin;
