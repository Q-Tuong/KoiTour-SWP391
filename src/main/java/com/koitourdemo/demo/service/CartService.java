package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Cart;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void removeItemFromCart(HttpSession session, UUID koiId) {
        Cart cart = getCart(session);
        cart.removeItem(koiId);
        session.setAttribute(CART_SESSION_KEY, cart);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_SESSION_KEY);
    }

}
