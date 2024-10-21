import React, { useState } from "react";
import { userApi } from "../../api/userApi"; // Make sure this is still necessary
import logologin from "../../asset/logo/LK.png"; // If you're using this logo, consider adding it to the form
import "./resetpass.css";
import { NavLink, useNavigate } from "react-router-dom";
import axios from "axios";

function ResetPass() {
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const [passNew, setPassNew] = useState('');
  const [confirmPass, setConfirmPass] = useState('');
  const [loading, setLoading] = useState(false); // New loading state
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage(''); // Clear previous messages
    setLoading(true); // Start loading

    try {
      const response = await axios.post('http://14.225.212.120:8080/api/user/login', { // Ensure this is the correct endpoint
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
          <label htmlFor="passwordOld" className="form-label">
          Previous password:
          </label>
          <input
            type="password"
            id="passwordold"
            value={password}
            onChange={(event) => {
              setPassword(event.target.value);
              setMessage(''); // Clear message when user types
            }}
            className="form-input"
          />
        </div>
        <div className="form-group">
          <label htmlFor="passwordNew" className="form-label">
            New password:
          </label>
          <input
            type="password"
            id="passwordnew"
            value={passNew}
            onChange={(event) => {
              setPassNew(event.target.value);
              setMessage(''); // Clear message when user types
            }}
            className="form-input"
          />
        </div>
        <div className="form-group">
          <label htmlFor="confirmPass" className="form-label">
            Confirm Password:
          </label>
          <input
            type="password"
            id="confirmPass"
            value={confirmPass}
            onChange={(event) => {
              setConfirmPass(event.target.value);
              setMessage(''); // Clear message when user types
            }}
            className="form-input"
          />
        </div>
        <span><NavLink to="/signin">
              <button className="form-button2 ml-3">Submit</button>
            </NavLink>
        </span>
      </form>
    </div>
  );
}

export default ResetPass;
