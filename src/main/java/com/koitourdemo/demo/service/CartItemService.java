package com.koitourdemo.demo.service;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.entity.CartItem;
import com.koitourdemo.demo.entity.Koi;
import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.repository.CartItemRepository;
import com.koitourdemo.demo.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    CartRepository cartRepository;

    @Autowired
    CartService cartService;

    @Autowired
    KoiService koiService;

    public void addItemToCart(Long cartId, UUID koiId, int quantity) {
        //1. Get the cart
        //2. Get the product
        //3. Check if the product already in the cart
        //4. If Yes, then increase the quantity with the requested quantity
        //5. If No, then initiate a new CartItem entry.
        Cart cart = cartService.getCart(cartId);
        Koi koi = koiService.getKoiById(koiId);
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getKoi().getId().equals(koiId))
                .findFirst().orElse(new CartItem());
        if (cartItem.getId() == null) {
            cartItem.setCart(cart);
            cartItem.setKoi(koi);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(koi.getPrice());
        }
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    public void removeItemFromCart(Long cartId, UUID koiId) {
        Cart cart = cartService.getCart(cartId);
        CartItem itemToRemove = getCartItem(cartId, koiId);
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    public void updateItemQuantity(Long cartId, UUID koiId, int quantity) {
        Cart cart = cartService.getCart(cartId);
        cart.getCartItems()
                .stream()
                .filter(item -> item.getKoi().getId().equals(koiId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getKoi().getPrice());
                    item.setTotalPrice();
                });
        BigDecimal totalAmount = cart.getCartItems()
                .stream().map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
    }

    public CartItem getCartItem(Long cartId, UUID koiId) {
        Cart cart = cartService.getCart(cartId);
        return  cart.getCartItems()
                .stream()
                .filter(item -> item.getKoi().getId().equals(koiId))
                .findFirst().orElseThrow(() -> new NotFoundException("Item not found"));
    }
}
