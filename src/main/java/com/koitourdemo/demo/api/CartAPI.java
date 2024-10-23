package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
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
    HttpSession session;

    @Autowired
    CartService cartService;

    @GetMapping("/my-cart")
    public ResponseEntity getCart() {
        Cart cart = cartService.getCart(session);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/clear")
    public ResponseEntity clearCart() {
        cartService.clearCart(session);
        return ResponseEntity.ok("Clear Cart Success!");
    }

    @GetMapping("/total-price")
    public ResponseEntity getTotalAmount() {
        BigDecimal totalPrice = cartService.getTotalPrice(session);
        return ResponseEntity.ok(totalPrice);
    }

}
