package com.aniket.FliprBackendDevelopmentTask.repo;

import com.aniket.FliprBackendDevelopmentTask.model.Order;
import com.aniket.FliprBackendDevelopmentTask.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
}

