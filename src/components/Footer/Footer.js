import React from "react";
import "./Footer.css";
import Logo from "../../asset/logo/logo1.png";
import { NavLink } from "react-router-dom";
import { AiOutlineHome } from "react-icons/ai";
import { AiOutlineMail } from "react-icons/ai";
import Carticon from "../../asset/img/payment-icon.webp";

export default function Footer() {
  return (
    <div className="mt-24">
      <div>
        <footer className="footer-distributed ">
          <div className="footer-left">
            <NavLink to="/home">
              <img
                style={{ maxWidth: "113px", marginLeft: "90px" }}
                src={Logo}
                alt="Logo footer"
              />
            </NavLink>
            <p className="footer-links">
            <NavLink to="/home" className={"link-1"}>
            Home
          </NavLink>
            <NavLink to="/kois">
            Kois
          </NavLink>
  
          <NavLink to="/koifarms">
            Koifarms
          </NavLink>
          
          <NavLink  to="/tours">
            Tours
          </NavLink>
            </p>
            <p className="footer-company-name">Loyalty Koi</p>
          </div>
          <div className="footer-center">
            <div>
              <i className="fa fa-map-marker" />
              <p style={{ marginTop: "66px" }}>
                <span className="icon">
                  <AiOutlineHome />
                </span>
                <span style={{ marginTop: "-20px", marginLeft: "24px" }}>
                  Địa chỉ: HCM
                </span>
                <br />
                <span>
                  <AiOutlineMail />
                  <p style={{ marginTop: "-44px", marginLeft: "24px" }}>
                    {" "}
                    Email: 08111111111gmail.com
                  </p>
                </span>
              </p>
            </div>
            <div>
              <i className="fa fa-phone" />
            </div>
          </div>
          <div className="footer-right">
            <p className="footer-company-about">
              <span>Chứng nhận</span>
              Các chứng nhận của công ty
            </p>
            <div className="footer-icons">
              <img src={Carticon} alt="cart-icon" />
            </div>
          </div>
        </footer>
      </div>
    </div>
  );
}
