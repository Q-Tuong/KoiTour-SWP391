package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.repository.CartItemRepository;
import com.koitourdemo.demo.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    public Cart getCart(long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cart not found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    @Transactional
    public void clearCart(long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    public BigDecimal getTotalPrice(long id) {
        Cart cart = getCart(id);
        return cart.getTotalAmount();
    }

    public Long initializeNewCart() {
        Cart newCart = new Cart();
        AtomicLong cartIdGenerator = new AtomicLong(0);
        long newCartId = cartIdGenerator.incrementAndGet();
        newCart.setId(newCartId);
        return cartRepository.save(newCart).getId();
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

}
