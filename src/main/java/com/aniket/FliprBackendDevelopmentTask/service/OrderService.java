package com.aniket.FliprBackendDevelopmentTask.service;


import com.aniket.FliprBackendDevelopmentTask.model.*;
import com.aniket.FliprBackendDevelopmentTask.repo.CartItemRepo;
import com.aniket.FliprBackendDevelopmentTask.repo.OrderRepo;
import com.aniket.FliprBackendDevelopmentTask.repo.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    @Autowired
    private final OrderRepo orderRepo;
    @Autowired
    private final CartItemRepo cartItemRepo;
    @Autowired
    private final UserRepo userRepo;
    private User user;

    public List<Order> getOrders(User user) {
        return orderRepo.findByUser(user);
    }

    public Order getOrder(Long id) {
        Optional<Order> orderOptional = orderRepo.findById(id);
        if (!orderOptional.isPresent()) {
            throw new RuntimeException("Order not found");
        }
        return orderOptional.get();
    }

    public Order createOrder(User user) {
        List<CartItem> cartItems = cartItemRepo.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = Order.builder()
                .user(user)
                .shippingAddress(user.getAddress())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .build();

        List<OrderItem> orderItems = cartItems.stream()
                .map(cartItem -> OrderItem.builder()
                        .order(order)
                        .product(cartItem.getProduct())
                        .quantity(cartItem.getQuantity())
                        .price(cartItem.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());

        order.setItems(orderItems);
        Order savedOrder = orderRepo.save(order);

        // Clear cart after order is created
        cartItemRepo.deleteAll(cartItems);

        return savedOrder;
    }
}

