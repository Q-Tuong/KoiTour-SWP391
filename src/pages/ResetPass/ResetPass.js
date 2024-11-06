import React, { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import "./resetpass.css"; // Create this CSS file for styling if needed

function ResetPass() {
  const [email, setEmail] = useState("");
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    setMessage(""); // Clear previous messages
    setLoading(true); // Start loading

    try {
      const response = await axios.post(
        "http://14.225.212.120:8080/api/user/forgot-password",
        { email }
      );
      setMessage("Password reset link sent to your email!");
      // Optionally, navigate to another page after successful submission
      // navigate("/login");
    } catch (error) {
      console.error("Error sending reset password email:", error);
      setMessage("Failed to send email. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="forgot-password-container">
      <h2>Quên mật khẩu</h2>
      <form onSubmit={handleSubmit} className="forgot-password-form">
        <div className="form-group">
          <label htmlFor="email" className="form-label">
            Email:
          </label>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(event) => {
              setEmail(event.target.value);
              setMessage(""); // Clear message when user types
            }}
            className="form-input"
            required
          />
        </div>
        <button type="submit" className="form-button" disabled={loading}>
          {loading ? "Sending..." : "Send Reset Link"}
        </button>
        <div className="form-message">{message}</div>
      </form>
    </div>
  );
}

export default ResetPass;
