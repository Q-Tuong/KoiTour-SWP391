import React from "react";
import "./home.css";
import image1 from "../../asset/img/image1.png";
import image2 from "../../asset/img/image2.png";
import image3 from "../../asset/img/image3.png";
import image4 from "../../asset/img/image4.png";
import image5 from "../../asset/img/image5.png";
import image6 from "../../asset/img/image6.png";
import { NavLink } from "react-router-dom";
import BestSeller from "../Best Seller/BestSeller";

const Home = () => {
  return (
    <div>
      <div className="grid-container mt-16">
        <div className="grid-item">
          <NavLink to="/shirt">
            <img src={image1} alt="image-1" className="img-fluid" />
            <div className="caption">
              <h1>
                Áo / TOP
                <br />
                <p>SHOP NOW</p>
              </h1>
            </div>
          </NavLink>
        </div>
        <div className="grid-item">
          <NavLink to="/lakstudio">
            <img src={image2} alt="image-1" className="img-fluid" />
            <div className="caption">
              <h1>
                LAKSTUDIOS
                <br />
                <p>BRAND DESIGN / SHOP NOW</p>
              </h1>
            </div>
          </NavLink>
        </div>
        <NavLink to="/accessory">
          <div className="grid-item">
            <img src={image3} alt="image-3" className="img-fluid" />
            <div className="caption">PHỤ KIỆN / ACCESSORY</div>
          </div>
        </NavLink>
        <div className="grid-item">
          <NavLink to="/shoes">
            <img src={image4} alt="image-1" className="img-fluid" />
            <div className="caption">
              <h1>
                GIÀY / SHOES
                <br />
                <p>SHOP NOW</p>
              </h1>
            </div>
          </NavLink>
        </div>
        <div className="grid-item">
          <NavLink to="/trouser">
            <img src={image5} alt="image-1" className="img-fluid" />
            <div className="caption">
              <h1>
                QUẦN
                <br />
                <p>SHOP NOW</p>
              </h1>
            </div>
          </NavLink>
        </div>
        <div className="grid-item last-grid-item">
          <img src={image6} alt="image-6" className="img-fluid" />
        </div>
      </div>
      <div>
        <BestSeller />
      </div>
    </div>
  );
};

export default Home;
