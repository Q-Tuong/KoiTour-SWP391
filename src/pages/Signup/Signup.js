import React, { useState } from "react";
import axios from "axios";
import "./signup.css";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";

function Signup() {
  const [firstname, setFirstName] = useState("");
  const [lastname, setLastName] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [message, setMessage] = useState("");
  const [phone, setPhone] = useState("");
  const [email, setEmail] = useState("");
  const [address, setAddress] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (event) => {
    event.preventDefault();

    // Validate input fields
    if (!firstname) {
      setMessage("Vui lòng nhập tên");
      return;
    }

    if (!lastname) {
      setMessage("Vui lòng nhập họ");
      return;
    }

    if (!email || !password || !phone || !confirmPassword || !address) {
      setMessage("Vui lòng nhập đầy đủ thông tin");
      return;
    }

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      setMessage("Email không hợp lệ");
      return;
    }

    const phoneRegex = /^(07070305050|\d{10,15})$/; // Adjust as needed
    if (!phoneRegex.test(phone)) {
      setMessage("Số điện thoại không hợp lệ");
      return;
    }

    if (password !== confirmPassword) {
      setMessage("Mật khẩu không khớp");
      return;
    }

    setLoading(true); // Start loading
    try {
      const response = await axios.post('http://14.225.212.120:8080/api/user/register', {
        firstName: firstname,
        lastName: lastname,
        email,
        phone,
        password,
        address,
        createAt: new Date().toISOString() // Current timestamp
      });

      const { token, id } = response.data;

      const user = { email, firstname, lastname, phone, address, token, id };
      localStorage.setItem("signup", JSON.stringify(user));
      localStorage.setItem("id", id);

      setMessage(`Đăng ký thành công với email: ${user.email}`);

      await sendVerificationEmail(token); // Send the token for verification

      clearFields();
    } catch (error) {
      setMessage(`Đăng ký thất bại: ${error.response ? error.response.data : error.message}`);
    } finally {
      setLoading(false); // Stop loading
    }
  };

  const sendVerificationEmail = async (token) => {
    try {
      await axios.post('http://14.225.212.120:8080/api/user/verify', {
        token: token // Send the token instead of the email
      });
      setMessage("Email xác thực đã được gửi.");
    } catch (error) {
      setMessage(`Không thể gửi email xác thực: ${error.response?.data || error.message}`);
    }
  };

  const clearFields = () => {
    setFirstName("");
    setLastName("");
    setPassword("");
    setConfirmPassword("");
    setPhone("");
    setEmail("");
    setAddress("");
  };

  return (
    <div>
      <Header />
      <div className="login-container" style={{ margin: '10% 0%' }}>
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label htmlFor="firstname" className="form-label">First Name:</label>
            <input type="text" id="firstname" value={firstname} onChange={(event) => setFirstName(event.target.value)} className="form-input" required />
          </div>
          <div className="form-group">
            <label htmlFor="lastname" className="form-label">Last Name:</label>
            <input type="text" id="lastname" value={lastname} onChange={(event) => setLastName(event.target.value)} className="form-input" required />
          </div>
          <div className="form-group">
            <label htmlFor="password" className="form-label">Mật khẩu:</label>
            <input type="password" id="password" value={password} onChange={(event) => setPassword(event.target.value)} className="form-input" required autocomplete="new-password" />
          </div>
          <div className="form-group">
            <label htmlFor="confirmPassword" className="form-label">Xác nhận mật khẩu:</label>
            <input type="password" id="confirmPassword" value={confirmPassword} onChange={(event) => setConfirmPassword(event.target.value)} className="form-input" required autocomplete="new-password" />
          </div>
          <div className="form-group">
            <label htmlFor="email" className="form-label">Email:</label>
            <input type="email" id="email" value={email} onChange={(event) => setEmail(event.target.value)} className="form-input" required />
          </div>
          <div className="form-group">
            <label htmlFor="phone" className="form-label">Phone:</label>
            <input type="text" id="phone" value={phone} onChange={(event) => setPhone(event.target.value)} className="form-input" required />
          </div>
          <div className="form-group">
            <label htmlFor="address" className="form-label">Address:</label>
            <input type="text" id="address" value={address} onChange={(event) => setAddress(event.target.value)} className="form-input" required />
          </div>

          <button className="form-button" type="submit" disabled={loading}>
            {loading ? 'Loading...' : 'Signup'}
          </button>
          <div className="form-message">{message}</div>
        </form>
      </div>
      <Footer />
    </div>
  );
}

export default Signup;
