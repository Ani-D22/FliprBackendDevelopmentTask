package com.aniket.FliprBackendDevelopmentTask.repo;

import com.aniket.FliprBackendDevelopmentTask.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
}
