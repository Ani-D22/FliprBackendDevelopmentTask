package com.aniket.FliprBackendDevelopmentTask.service;


import com.aniket.FliprBackendDevelopmentTask.model.CartItem;
import com.aniket.FliprBackendDevelopmentTask.model.Product;
import com.aniket.FliprBackendDevelopmentTask.model.User;
import com.aniket.FliprBackendDevelopmentTask.repo.CartItemRepo;
import com.aniket.FliprBackendDevelopmentTask.repo.ProductRepo;
import com.aniket.FliprBackendDevelopmentTask.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CartService {
    @Autowired
    private final CartItemRepo cartItemRepo;
    @Autowired
    private final ProductRepo productRepo;
    @Autowired
    private final UserRepo userRepo;

    public List<CartItem> getCartItems(User user) {
        return cartItemRepo.findByUser(user);
    }

    public CartItem addToCart(User user, Long productId, Integer quantity) {
        if (user == null || user.getUser_id() == null) {
            throw new RuntimeException("User must be authenticated");
        }

        User managedUser = userRepo.getReferenceById(user.getUser_id());
        Product product = productRepo.getReferenceById(productId);

        // Check for existing cart item
        Optional<CartItem> existingItem = cartItemRepo.findByUserAndProduct(managedUser, product);

        if (existingItem.isPresent()) {
            CartItem cartItem = existingItem.get();
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            return cartItemRepo.save(cartItem);
        }

        // Create new cart item
        CartItem newCartItem = new CartItem();
        newCartItem.setUser(managedUser);
        newCartItem.setProduct(product);
        newCartItem.setQuantity(quantity);

        return cartItemRepo.save(newCartItem);
    }

    public CartItem updateCartItem(User user, Long productId, Integer quantity) {
        CartItem cartItem = cartItemRepo.findByUserAndProduct(user, productRepo.getReferenceById(productId))
                .orElseThrow(() -> new RuntimeException("Cart item not found for this user"));

        cartItem.setQuantity(quantity);
        return cartItemRepo.save(cartItem);
    }

    public void removeFromCart(Long cartItemId) {
        cartItemRepo.deleteById(cartItemId);
    }
}
