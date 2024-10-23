package com.koitourdemo.demo.api;

import com.koitourdemo.demo.entity.Cart;
import com.koitourdemo.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping("api/cart")
@RestController
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CartAPI {

    @Autowired
    CartService cartService;

    @GetMapping("/get")
    public ResponseEntity<?> getCart(HttpSession session) {
        try {
            Cart cart = cartService.getCart(session);
            if (cart == null || cart.getItems().isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(new Cart()); // Trả về cart rỗng thay vì null
            }
            return ResponseEntity.ok(cart);
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Error getting cart: " + e.getMessage());
        }
    }

    @PostMapping("/{koiId}/add")
    public ResponseEntity addItem(@RequestParam UUID koiId, @RequestParam int quantity, HttpSession session) {
        cartService.addItemToCart(session, koiId, quantity);
        return ResponseEntity.ok("Item added to cart");
    }

    @DeleteMapping("/{koiId}/remove")
    public ResponseEntity removeItem(@PathVariable UUID koiId, HttpSession session) {
        cartService.removeItemFromCart(session, koiId);
        return ResponseEntity.ok("Item removed from cart");
    }

    @PutMapping("/{koiId}/update-quantity")
    public ResponseEntity updateQuantity(@PathVariable UUID koiId, @RequestParam int quantity, HttpSession session) {
        cartService.updateItemQuantity(session, koiId, quantity);
        return ResponseEntity.ok("Quantity updated");
    }

    @DeleteMapping("/clear")
    public ResponseEntity clearCart(HttpSession session) {
        cartService.clearCart(session);
        return ResponseEntity.ok("Cart cleared");
    }

    @GetMapping("/total-price")
    public ResponseEntity getTotal(HttpSession session) {
        return ResponseEntity.ok(cartService.getTotalPrice(session));
    }

}
