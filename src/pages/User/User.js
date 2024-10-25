import { useState } from "react";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faEye, faEyeSlash } from "@fortawesome/free-solid-svg-icons";
import { reasetpasswordApi } from "../../api/reasetpasswordApi";
import "./user.css";

function User() {
  const username = localStorage.getItem("username");
  const [passwordVisible, setPasswordVisible] = useState(false);
  const [passwordValue, setPasswordValue] = useState(
    localStorage.getItem("password")
  );
  const [changingPassword, setChangingPassword] = useState(false);
  const [newPasswordValue, setNewPasswordValue] = useState("");
  const [showSuccessMessage, setShowSuccessMessage] = useState(false);
  const [id, setid] = useState(localStorage.getItem("id") || "");

  const handleTogglePassword = () => {
    setPasswordVisible(!passwordVisible);
  };

  const handlePasswordChange = (event) => {
    setPasswordValue(event.target.value);
  };

  const handlePasswordChangeButtonClick = () => {
    setChangingPassword(true);
  };

  const handleNewPasswordChange = (event) => {
    setNewPasswordValue(event.target.value);
  };

  const handleSaveButtonClick = async () => {
    try {
      const id = localStorage.getItem("id");
      if (!id) {
        console.log("User ID is null"); // Handle error here
        return;
      }
      const response = await reasetpasswordApi.updatePassword(
        id,
        newPasswordValue
      );
      if (response.status === 200) {
        setPasswordValue(newPasswordValue); // Lưu mật khẩu mới vào state
        setNewPasswordValue("");
        setChangingPassword(false);
        setShowSuccessMessage(true);
        setTimeout(() => setShowSuccessMessage(false), 3000); // Auto hide message after 3 seconds

        // Cập nhật mật khẩu mới vào localStorage
        localStorage.setItem("password", newPasswordValue);
      } else {
        console.log(response.data); // Handle error response here
      }
    } catch (error) {
      console.log(error); // Handle error here
    }
  };

  return (
    <div className="container">
      <h1 className="title">Thông tin người dùng</h1>
      <img
        src="https://static.vecteezy.com/system/resources/previews/002/002/403/original/man-with-beard-avatar-character-isolated-icon-free-vector.jpg"
        alt="Avatar"
        className="avatar-image"
      />
      <p className="username">Tên đăng nhập: {username}</p>
      <div className="password-container">
        <label htmlFor="password-input" className="password-label">
          Mật khẩu:
        </label>
        <div className="password-input-container">
          <input
            type={passwordVisible ? "text" : "password"}
            value={passwordValue}
            onChange={handlePasswordChange}
            readOnly={!passwordVisible}
            id="password-input"
            className="password-input"
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
          <button className="save-button" onClick={handleSaveButtonClick}>
            Lưu thông tin
          </button>
        </div>
      )}
      {showSuccessMessage && (
        <div className="success-message">Thay đổi mật khẩu thành công</div>
      )}
    </div>
  );
}

export default User;
