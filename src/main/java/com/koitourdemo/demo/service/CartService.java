package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.entity.CartItem;
import com.koitourdemo.demo.entity.Koi;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class CartService {

    private static final String CART_SESSION_KEY = "CART_SESSION";

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
            log.error("Error getting cart from session: {}", e.getMessage());
            return new Cart();
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

        cart.addItem(item);
        session.setAttribute(CART_SESSION_KEY, cart);
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
