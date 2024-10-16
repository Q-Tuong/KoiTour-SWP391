package com.koitourdemo.demo.api;

import com.koitourdemo.demo.exception.NotFoundException;
import com.koitourdemo.demo.model.response.ApiResponse;
import com.koitourdemo.demo.service.CartItemService;
import com.koitourdemo.demo.service.CartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.HttpStatus.NOT_FOUND;

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
    public ResponseEntity<ApiResponse> addItemToCart(@RequestParam(required = false) Long cartId,
                                                     @RequestParam UUID koiId,
                                                     @RequestParam Integer quantity) {
        try {
            if (cartId == null) {
                cartId= cartService.initializeNewCart();
            }
            cartItemService.addItemToCart(cartId, koiId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add Item Success", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed", null));
        }
    }

    @DeleteMapping("/{cartId}/koi/{koiId}/remove")
    public ResponseEntity<ApiResponse> removeItemFromCart(@PathVariable Long cartId, @PathVariable UUID koiId) {
        try {
            cartItemService.removeItemFromCart(cartId, koiId);
            return ResponseEntity.ok(new ApiResponse("Remove Item Success", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed", null));
        }
    }

    @PutMapping("/{cartId}/koi/{koiId}/update")
    public  ResponseEntity<ApiResponse> updateItemQuantity(@PathVariable Long cartId,
                                                           @PathVariable UUID koiId,
                                                           @RequestParam Integer quantity) {
        try {
            cartItemService.updateItemQuantity(cartId, koiId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update Item Success", null));
        } catch (NotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Failed", null));
        }
    }

}
