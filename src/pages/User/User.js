import { useState, useEffect } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import axios from "axios";
import "./user.css";
import Header from "../../components/Header/Header";
import Footer from "../../components/Footer/Footer";
function User() {
  const [userData, setUserData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    phone: "",
    address: "",
  });
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [passwordValue, setPasswordValue] = useState("");
  const [changingPassword, setChangingPassword] = useState(false);
  const [newPasswordValue, setNewPasswordValue] = useState("");
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [isSavingPassword, setIsSavingPassword] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const userId = localStorage.getItem("id");
  const token = localStorage.getItem("token");

  useEffect(() => {
    const fetchUserData = async () => {
      if (!userId || !token) {
        console.error("User ID or token is missing");
        return;
      }

      try {
        const response = await axios.get(
          `http://14.225.212.120:8080/api/user/me/${userId}`,
          {
            headers: {
              Authorization: `Bearer ${token}`,
            },
          }
        );
        setUserData(response.data);
      } catch (error) {
        console.error("Failed to fetch user data", error);
      }
    };

    fetchUserData();
  }, [userId, token]);

  const handleTogglePassword = () => {
    setPasswordVisible((prev) => !prev);
  };

  const handlePasswordChangeButtonClick = () => {
    setChangingPassword(true);
  };

  const handleNewPasswordChange = (event) => {
    setNewPasswordValue(event.target.value);
  };

  const handleSaveButtonClick = async () => {
    if (!newPasswordValue) {
      console.error("New password cannot be empty");
      return;
    }

    setIsSavingPassword(true);
    setErrorMessage("");

    try {
      const response = await axios.post(
        "http://14.225.212.120:8080/api/user/reset-password",
        {
          password: newPasswordValue,
        },
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        setPasswordValue(newPasswordValue);
        setNewPasswordValue("");
        setChangingPassword(false);
        setShowSuccessMessage(true);
        setTimeout(() => setShowSuccessMessage(false), 3000);
      } else {
        console.error("Failed to update password");
        setErrorMessage("Failed to update password. Please try again.");
      }
    } catch (error) {
      console.error("Error updating password:", error);
      setErrorMessage(
        "Error updating password: " +
          (error.response?.data?.message || "An error occurred.")
      );
    } finally {
      setIsSavingPassword(false);
    }
  };

  const handleUpdateUserData = async (updatedData) => {
    try {
      const response = await axios.put(
        `http://14.225.212.120:8080/api/user/update/${userId}`,
        updatedData,
        {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      );

      if (response.status === 200) {
        setUserData(response.data);
        setShowSuccessMessage(true);
        setTimeout(() => setShowSuccessMessage(false), 3000);
      } else {
        setErrorMessage("Failed to update user information. Please try again.");
      }
    } catch (error) {
      console.error("Error updating user data:", error);
      setErrorMessage(
        "Error updating user data: " +
          (error.response?.data?.message || "An error occurred.")
      );
    }
  };

  return (
    <div> <Header />
    <div className="container">
      <h1 className="title">Thông tin người dùng</h1>
      <img
        src="https://static.vecteezy.com/system/resources/previews/002/002/403/original/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        alt="Avatar"
        className="avatar-image"
      />
      <p>First name: {userData.firstName || "N/A"}</p>
      <p>Last Name: {userData.lastName || "N/A"}</p>
      <p>Email: {userData.email || "N/A"}</p>
      <p>Phone: {userData.phone || "N/A"}</p>
      <p>Address: {userData.address || "N/A"}</p>

      <div>
        <form
          onSubmit={(e) => {
            e.preventDefault();
            const updatedData = {
              firstName: userData.firstName,
              lastName: userData.lastName,
              email: userData.email,
              phone: userData.phone,
              address: userData.address,
            };
            handleUpdateUserData(updatedData);
          }}
        >
          <div className="mb-3">
            <label htmlFor="firstName" className="form-label">
              Họ
            </label>
            <input
              type="text"
              className="form-control"
              id="firstName"
              value={userData.firstName}
              onChange={(e) =>
                setUserData({ ...userData, firstName: e.target.value })
              }
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="lastName" className="form-label">
              Tên
            </label>
            <input
              type="text"
              className="form-control"
              id="lastName"
              value={userData.lastName}
              onChange={(e) =>
                setUserData({ ...userData, lastName: e.target.value })
              }
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="email" className="form-label">
              Email
            </label>
            <input
              type="email"
              className="form-control"
              id="email"
              value={userData.email}
              onChange={(e) =>
                setUserData({ ...userData, email: e.target.value })
              }
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="phone" className="form-label">
              Điện thoại
            </label>
            <input
              type="tel"
              className="form-control"
              id="phone"
              value={userData.phone}
              onChange={(e) =>
                setUserData({ ...userData, phone: e.target.value })
              }
              required
            />
          </div>
          <div className="mb-3">
            <label htmlFor="address" className="form-label">
              Địa chỉ
            </label>
            <input
              type="text"
              className="form-control"
              id="address"
              value={userData.address}
              onChange={(e) =>
                setUserData({ ...userData, address: e.target.value })
              }
              required
            />
          </div>
          <button type="submit" className="btn btn-primary">
            Lưu thông tin
          </button>
        </form>
      </div>
      <div className="password-container">
        <label htmlFor="password-input" className="password-label">
          Mật khẩu:
        </label>
        <div className="password-input-container">
          <input
            type={passwordVisible ? "text" : "password"}
            value={passwordValue}
            id="password-input"
            className="password-input"
            readOnly
          />
          <button
            className={`toggle-password ${passwordVisible ? "visible" : ""}`}
            onClick={handleTogglePassword}
          >
            <FontAwesomeIcon icon={passwordVisible ? faEye : faEyeSlash} />
          </button>
          <button
            className="change-password-button"
            onClick={handlePasswordChangeButtonClick}
          >
            Thay đổi mật khẩu
          </button>
        </div>
      </div>

      {changingPassword && (
        <div className="new-password-container">
          <label htmlFor="new-password-input" className="password-label">
            Mật khẩu mới:
          </label>
          <div className="password-input-container">
            <input
              type="password"
              value={newPasswordValue}
              onChange={handleNewPasswordChange}
              id="new-password-input"
              className="password-input"
            />
          </div>
          <button
            className="save-button"
            onClick={handleSaveButtonClick}
            disabled={isSavingPassword}
          >
            {isSavingPassword ? "Saving..." : "Lưu thông tin"}
          </button>
        </div>
      )}

      {showSuccessMessage && (
        <div className="success-message">Thay đổi mật khẩu thành công</div>
      )}

      {errorMessage && <div className="error-message">{errorMessage}</div>}
    </div>
    <Footer />
    </div>
  );
}

export default User;
