package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CartAPI {

    @Autowired
    CartService cartService;

    @GetMapping("/{cartId}/my-cart")
    public ResponseEntity getCart(@PathVariable long cartId) {
        Cart cart = cartService.getCart(cartId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity clearCart( @PathVariable long cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.ok("Clear Cart Success!");
    }

    @GetMapping("/{cartId}/total-price")
    public ResponseEntity getTotalAmount( @PathVariable Long cartId) {
            BigDecimal totalPrice = cartService.getTotalPrice(cartId);
            return ResponseEntity.ok(totalPrice);
    }
}
