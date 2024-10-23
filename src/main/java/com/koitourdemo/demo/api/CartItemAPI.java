package com.koitourdemo.demo.api;

import com.koitourdemo.demo.service.CartItemService;
import com.koitourdemo.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/cart-item")
@CrossOrigin("*")
@SecurityRequirement(name = "api")
public class CartItemAPI {

    @Autowired
    CartService cartService;

    @Autowired
    CartItemService cartItemService;

    @PostMapping("/add")
    public ResponseEntity addItemToCart(@RequestParam UUID koiId,
                                        @RequestParam Integer quantity) {
        cartItemService.addItemToCart(koiId, quantity);
        return ResponseEntity.ok("Add koi Successfully!");
    }

    @DeleteMapping("/koi/{koiId}/remove")
    public ResponseEntity removeItemFromCart(@PathVariable UUID koiId) {
        cartItemService.removeItemFromCart(koiId);
        return ResponseEntity.ok("Remove koi from cart Successfully!");
    }

    @PutMapping("/koi/{koiId}/update")
    public ResponseEntity updateItemQuantity(@PathVariable UUID koiId,
                                             @RequestParam Integer quantity) {
        cartItemService.updateItemQuantity(koiId, quantity);
        return ResponseEntity.ok("Update koi quantity Successfully!");
    }

}
