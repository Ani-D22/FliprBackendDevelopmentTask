package com.aniket.FliprBackendDevelopmentTask.repo;

import com.aniket.FliprBackendDevelopmentTask.model.CartItem;
import com.aniket.FliprBackendDevelopmentTask.model.Product;
import com.aniket.FliprBackendDevelopmentTask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProduct(User user, Product product);

    @Query("SELECT ci FROM CartItem ci WHERE ci.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);
}
