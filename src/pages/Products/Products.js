import { useNavigate, useParams } from "react-router-dom";
import ProductApi from "../../api/productApi";
import { useEffect, useState } from "react";
import "./product.css";

const Products = () => {
  const { id } = useParams();
  const [product, setProduct] = useState(null);
  const [cart, setCart] = useState([]); // danh sách các sản phẩm trong giỏ hàng
  const navigate = useNavigate();
  const buttons = document.querySelectorAll(".product-variation");

  const [selectedSize, setSelectedSize] = useState("S"); // default value is "S"

  buttons.forEach((button) => {
    button.addEventListener("click", () => {
      buttons.forEach((button) => {
        button.classList.remove("active");
      });
      button.classList.add("active");
    });
  });

  const [count, setCount] = useState(0);
  const [quantity, setQuantity] = useState(1);

  const handleIncrement = () => {
    setQuantity(quantity + 1);
  };

  const handleDecrement = () => {
    if (quantity > 1) {
      setQuantity(quantity - 1);
    }
  };
  const fetchProduct = async () => {
    try {
      const response = await ProductApi.get(id);
      setProduct(response);
    } catch (error) {
      console.log("fail", error);
    }
  };

  useEffect(() => {
    fetchProduct();
  }, [id]);

  function addToCart() {
    const item = {
      ...product,
      size: selectedSize,
      quantity,
    };

    // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
    const existingItem = cart.find(
      (cartItem) => cartItem.id === item.id && cartItem.size === item.size
    );

    if (existingItem) {
      // Nếu đã có sản phẩm trong giỏ hàng, tăng số lượng lên 1
      const updatedCart = cart.map((cartItem) =>
        cartItem.id === existingItem.id && cartItem.size === existingItem.size
          ? { ...cartItem, quantity: cartItem.quantity + 1 }
          : cartItem
      );
      setCart(updatedCart);
    } else {
      // Nếu sản phẩm chưa có trong giỏ hàng, thêm vào giỏ hàng
      setCart([...cart, item]);
    }

    setSelectedSize("S"); // reset the selected size to the default
    navigate("/cart");

    // Lưu thông tin giỏ hàng vào localStorage
    const savedCart = JSON.parse(localStorage.getItem("cart")) || [];
    const cartIndex = savedCart.findIndex(
      (cartItem) => cartItem.id === item.id && cartItem.size === item.size
    );
    if (cartIndex >= 0) {
      savedCart[cartIndex].quantity += quantity;
    } else {
      savedCart.push(item);
    }
    localStorage.setItem("cart", JSON.stringify(savedCart));
  }

  function handleSizeSelection(size, newQuantity) {
    setSelectedSize(size);
    const updatedCart = cart.map((item) => {
      if (item.id === product.id && item.size === size) {
        // update the item's quantity if it's already in the cart
        return { ...item, quantity: newQuantity };
      } else {
        return item;
      }
    });
    setCart(updatedCart);
  }
  if (!product) {
    return <div>Loading...</div>;
  }

  return (
    <div>
      <div className="product-page">
        <div className="product-image">
          <img src={product.imgUrl} alt={product.name} />
        </div>
        <div className="product-details">
          <h3 className="name-product">{product.name}</h3>
          <p className="price-product">
            <span
              style={{ color: "#000", fontSize: "15px", fontWeight: "500" }}
            >
              Giá:
            </span>
            {product.price.toLocaleString("vi-VN")} ₫
          </p>
          <p className="mt-3">
            Trạng thái :{" "}
            <span className="text-red-600">Sản phẩm vẫn còn tại cửa hàng</span>
          </p>
          <div className="size">
            <p style={{ marginLeft: "278px" }}>Kích thước:</p>
            <div class="flex items-center bR6mEk sizee">
              <button
                class={`product-variation ${
                  selectedSize === "S" ? "active" : ""
                }`}
                data-value="S"
                onClick={() => handleSizeSelection("S")}
              >
                S
              </button>
              <button
                class={`product-variation ${
                  selectedSize === "M" ? "active" : ""
                }`}
                data-value="M"
                onClick={() => handleSizeSelection("M")}
              >
                M
              </button>
              <button
                class={`product-variation ${
                  selectedSize === "L" ? "active" : ""
                }`}
                data-value="L"
                onClick={() => handleSizeSelection("L")}
              >
                L
              </button>
              <button
                class={`product-variation ${
                  selectedSize === "XL" ? "active" : ""
                }`}
                data-value="XL"
                onClick={() => handleSizeSelection("XL")}
              >
                XL
              </button>
            </div>
            <p style={{ marginLeft: "287px", marginTop: "34px" }}>Số lượng:</p>
            <div
              style={{
                display: "flex",
                marginBottom: "-24px",

                marginTop: "10px",
              }}
            >
              <div className="flex ml-4" style={{ marginLeft: "259px" }}>
                <div style={{ padding: "20px" }}>
                  <button
                    style={{ padding: "20px" }}
                    className="minus"
                    onClick={handleIncrement}
                  >
                    <p className="p-minus">+</p>
                  </button>
                </div>
                <p style={{ marginTop: "36px" }}> {quantity}</p>
                <div style={{ padding: "20px" }}>
                  <button
                    style={{ padding: "20px" }}
                    className="plus"
                    onClick={handleDecrement}
                  >
                    <p className="p-plus">-</p>
                  </button>
                </div>
              </div>
            </div>
          </div>

          <div className="product-buttons ml-9">
            <button onClick={addToCart} className="add-to-cart">
              Add to Cart
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Products;
