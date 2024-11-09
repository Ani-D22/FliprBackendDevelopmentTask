package com.aniket.FliprBackendDevelopmentTask.controller;


import com.aniket.FliprBackendDevelopmentTask.model.CartItem;
import com.aniket.FliprBackendDevelopmentTask.model.User;
import com.aniket.FliprBackendDevelopmentTask.model.UserPrincipal;
import com.aniket.FliprBackendDevelopmentTask.repo.UserRepo;
import com.aniket.FliprBackendDevelopmentTask.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
class CartController {
    @Autowired
    private final CartService cartService;
    @Autowired
    private final UserRepo userRepo;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCartItems(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepo.findByEmail(userDetails.getUsername());
        System.out.println(userDetails);
        return ResponseEntity.ok(cartService.getCartItems(user));
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@AuthenticationPrincipal UserPrincipal userDetails, @RequestBody Map<String, Object> request) {

        User user = userRepo.findByEmail(userDetails.getUsername());

        Long productId = Long.parseLong(request.get("productId").toString());
        Integer quantity = Integer.parseInt(request.get("quantity").toString());
        return ResponseEntity.ok(cartService.addToCart(user, productId, quantity));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCart(@AuthenticationPrincipal UserPrincipal userDetails, @RequestBody Map<String, Object> request) {
        try {
            User user = userRepo.findByEmail(userDetails.getUsername());
            Long productId = Long.parseLong(request.get("productId").toString());
            Integer quantity = Integer.parseInt(request.get("quantity").toString());

            // Validate quantity is non-negative
            if (quantity < 0) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Quantity must be non-negative"));
            }

            // If quantity is 0, remove item from cart
            if (quantity == 0) {
                cartService.removeFromCart(productId);
                return ResponseEntity.ok(cartService.getCartItems(user));
            }

            CartItem updatedItem = cartService.updateCartItem(user, productId, quantity);
            return ResponseEntity.ok(updatedItem);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid product ID or quantity format"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> removeFromCart(@RequestBody Map<String, Object> request) {
        Long productId = Long.parseLong(request.get("productId").toString());
        cartService.removeFromCart(productId);
        return ResponseEntity.ok().build();
    }
}