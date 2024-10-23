package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.repository.CartItemRepository;
import com.koitourdemo.demo.repository.CartRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService {

    @Autowired
    HttpSession session;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    AuthenticationService authenticationService;

    public final String CART_SESSION_KEY = "CART_ID";

    public Cart getCart(HttpSession session) {
        Long cartId = (Long) session.getAttribute(CART_SESSION_KEY);
        if (cartId == null) {
            return initializeNewCart(session);
        }
        return cartRepository.findById(cartId)
                .orElseGet(() -> initializeNewCart(session));
    }

    @Transactional
    public void clearCart(HttpSession session) {
        Long cartId = (Long) session.getAttribute(CART_SESSION_KEY);
        if (cartId != null) {
            cartItemRepository.deleteAllByCartId(cartId);
            cartRepository.deleteById(cartId);
            session.removeAttribute(CART_SESSION_KEY);
        }
    }

    public BigDecimal getTotalPrice(HttpSession session) {
        Cart cart = getCart(session);
        return cart.getTotalAmount();
    }

    public Cart initializeNewCart(HttpSession session) {
        Cart newCart = new Cart();
        Cart savedCart = cartRepository.save(newCart);
        session.setAttribute(CART_SESSION_KEY, savedCart.getId());
        return savedCart;
    }

}
