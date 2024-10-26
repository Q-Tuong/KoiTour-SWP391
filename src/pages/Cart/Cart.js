import { useRef, useState } from "react";
import { useEffect } from "react";
import { Link, NavLink, useLocation } from "react-router-dom";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faTrash } from "@fortawesome/free-solid-svg-icons";
import { useNavigate } from "react-router-dom";

import "./cart.css";

export default function Cart() {
  const navigate = useNavigate();
  const handleCheckout = () => {
    navigate("/pay", { state: { cart } });
  };
  const location = useLocation();

  // Get initial cart from localStorage
  const getCartFromLocalStorage = () => {
    const cart = localStorage.getItem("cart");
    if (cart) {
      return JSON.parse(cart);
    } else {
      return [];
    }
  };

  // Set initial state from localStorage
  const [cart, setCart] = useState(getCartFromLocalStorage());

  // Save cart to localStorage when it changes
  useEffect(() => {
    localStorage.setItem("cart", JSON.stringify(cart));
  }, [cart]);

  // Handle adding a new product to the cart
  useEffect(() => {
    if (location.state && location.state.product) {
      const item = location.state.product;
      // Check if the product already exists in the cart
      const existingItem = cart.find((cartItem) => cartItem.id === item.id);
      if (existingItem) {
        // If it does, update the quantity of that product
        const updatedCart = cart.map((cartItem) => {
          if (cartItem.id === item.id) {
            return {
              ...cartItem,
              quantity: cartItem.quantity + item.quantity,
            };
          }
          return cartItem;
        });
        setCart(updatedCart);
      } else {
        // If not, add the product to the cart
        setCart([...cart, item]);
      }
    }
  }, [cart, location.state]);

  // Handle removing a product from the cart
  const removeFromCart = (id) => {
    const updatedCart = cart.filter((item) => item.id !== id);
    setCart(updatedCart);
  };

  // Handle incrementing the quantity of a product in the cart
  const handleIncrement = (id) => {
    const updatedCart = cart.map((item) => {
      if (item.id === id) {
        return {
          ...item,
          quantity: item.quantity + 1,
        };
      }
      return item;
    });
    setCart(updatedCart);
  };

  const handleDecrement = (id) => {
    const updatedCart = cart.map((item) => {
      if (item.id === id && item.quantity > 1) {
        return {
          ...item,
          quantity: item.quantity - 1,
        };
      }
      return item;
    });
    setCart(updatedCart);
  };

  const totalPrice = cart.reduce(
    (acc, item) => acc + item.price * item.quantity,
    0
  );

  return (
    <div>
      <div className="max-w-3xl mx-auto my-6 p-6 bg-white rounded-lg shadow-md">
        <h2 className="text-2xl font-semibold mb-6">Giỏ hàng</h2>
        {cart.map((item) => (
          <div
            key={item.id}
            className="flex flex-col sm:flex-row sm:justify-between items-center mb-4 pb-4 border-b sm:border-b-0 sm:pb-0"
          >
            <img
              className="w-48 h-48 object-cover rounded-lg sm:mr-6 mb-4 sm:mb-0"
              src={item.imgUrl}
              alt={item.name}
            />
            <div className="flex-1 flex flex-col justify-between sm:flex-row sm:items-center">
              <div className="flex-1">
                <h3
                  className="text-xl font-semibold"
                  style={{ fontWeight: "500", fontSize: "14px" }}
                >
                  {item.name}
                </h3>
                <p className="text-gray-600">{item.size}</p>
                <div className="flex items-center text-gray-600 text-sm">
                  <button
                    onClick={() => handleDecrement(item.id)}
                    className="p-1 border rounded"
                  >
                    -
                  </button>
                  <span className="mx-2">{item.quantity}</span>
                  <button
                    onClick={() => handleIncrement(item.id)}
                    className="p-1 border rounded"
                  >
                    +
                  </button>
                </div>
              </div>
              <div className="flex items-center">
                <span className="mr-2 font-semibold">
                  {item.price.toLocaleString("vi-VN")} ₫
                </span>
                <button
                  onClick={() => removeFromCart(item.id)}
                  className="text-red-500 hover:text-red-600 cursor-pointer"
                >
                  <FontAwesomeIcon icon={faTrash} />
                </button>
              </div>
            </div>
          </div>
        ))}
      </div>
      {cart.length > 0 && (
        <div className="max-w-3xl mx-auto my-6 p-6 bg-white rounded-lg shadow-md">
          <div className="flex justify-between font-semibold mb-4">
            <span>Tổng cộng:</span>
            <span>{totalPrice.toLocaleString("vi-VN")} ₫</span>
          </div>
          <div className="flex justify-end">
            <NavLink
              to="/"
              className="bg-red-400 text-white px-4 py-2 rounded-md hover:bg-red-600 mr-10 font-semibold text-sm"
            >
              Tiếp tục mua sắm
            </NavLink>
            <button
              onClick={handleCheckout}
              className="bg-orange-400 text-white px-4 py-2 rounded-md hover:bg-orange-500"
            >
              Thanh toán
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
