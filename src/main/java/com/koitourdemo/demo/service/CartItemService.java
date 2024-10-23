package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.entity.CartItem;
import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.entity.User;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.response.KoiResponse;
import com.koitourdemo.demo.repository.CartItemRepository;
import com.koitourdemo.demo.repository.CartRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    @Autowired
    HttpSession session;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    KoiService koiService;

    @Autowired
    AuthenticationService authenticationService;

    public void addItemToCart(UUID koiId, int quantity) {
        Cart cart = cartService.getCart(session);
        Koi koi = koiService.getKoiEntityById(koiId);

        CartItem existingItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getKoi().getName().equals(koi.getName()))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setTotalPrice();
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setKoi(koi);
            newItem.setProductName(koi.getName());
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(koi.getPrice());
            newItem.setTotalPrice();

            cart.getCartItems().add(newItem);
            cartItemRepository.save(newItem);
        }

        if (cart.getCustomer() == null) {
            User userRequest = authenticationService.getCurrentUser();
            cart.setCustomer(userRequest);
        }
        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

    public void removeItemFromCart(UUID koiId) {
        Cart cart = cartService.getCart(session);
        CartItem itemToRemove = getCartItem(koiId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    public void updateItemQuantity(UUID koiId, int quantity) {
        Cart cart = cartService.getCart(session);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getKoi().getId().equals(koiId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setTotalPrice();
                });
        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

    public CartItem getCartItem(UUID koiId) {
        Cart cart = cartService.getCart(session);
        return cart.getCartItems()
                .stream()
                .filter(item -> item.getKoi().getId().equals(koiId))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }

}
