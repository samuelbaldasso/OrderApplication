package com.sbaldasso.ecom.controller;

import com.sbaldasso.ecom.dto.CartItemRequest;
import com.sbaldasso.ecom.model.CartItem;
import com.sbaldasso.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartItemController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") Long userId, @RequestBody CartItemRequest request) {
        if(!cartService.addToCart(userId, request)){
            return ResponseEntity.badRequest().body("Product, user or stock not found.");
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<String> removeFromCart(@RequestHeader("X-User-ID") Long userId, @PathVariable Long id){
        if(!cartService.removeFromCart(userId, id)){
            return ResponseEntity.badRequest().body("Product, user or stock not found.");
        }else{
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> fetchCart(@RequestHeader("X-User-ID") Long userId){
        return ResponseEntity.ok(cartService.fetchCart(userId));
    }
}
