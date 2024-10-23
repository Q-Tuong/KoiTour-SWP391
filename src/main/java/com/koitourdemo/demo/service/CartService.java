package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.entity.CartItem;
import com.koitourdemo.demo.entity.Koi;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class CartService {

    public final String CART_SESSION_KEY = "CART_ID";
    public final Logger logger = LoggerFactory.getLogger(CartService.class);

    @Autowired
    private KoiService koiService;

    public Cart getCart(HttpSession session) {
        try {
            Cart cart = (Cart) session.getAttribute(CART_SESSION_KEY);
            if (cart == null) {
                cart = new Cart();
                session.setAttribute(CART_SESSION_KEY, cart);
            }
            return cart;
        } catch (Exception e) {
            // Log error
            logger.error("Error getting cart from session: " + e.getMessage());
            return new Cart(); // Trả về cart rỗng nếu có lỗi
        }
    }

    public void addItemToCart(HttpSession session, UUID koiId, int quantity) {
        Cart cart = getCart(session);
        Koi koi = koiService.getKoiEntityById(koiId);

        CartItem item = new CartItem();
        item.setKoiId(koiId);
        item.setProductName(koi.getName());
        item.setQuantity(quantity);
        item.setUnitPrice(koi.getPrice());
        item.updateTotalPrice();

        cart.addItem(item);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void removeItemFromCart(HttpSession session, UUID koiId) {
        Cart cart = getCart(session);
        cart.removeItem(koiId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void updateItemQuantity(HttpSession session, UUID koiId, int quantity) {
        Cart cart = getCart(session);
        cart.updateItemQuantity(koiId, quantity);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

    public BigDecimal getTotalPrice(HttpSession session) {
        return getCart(session).getTotalAmount();
    }

}
