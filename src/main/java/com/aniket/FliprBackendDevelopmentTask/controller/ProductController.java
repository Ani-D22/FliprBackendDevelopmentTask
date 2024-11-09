package com.aniket.FliprBackendDevelopmentTask.controller;


import com.aniket.FliprBackendDevelopmentTask.model.Product;
import com.aniket.FliprBackendDevelopmentTask.repo.ProductRepo;
import com.aniket.FliprBackendDevelopmentTask.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
class ProductController {
    @Autowired
    private final ProductService productService;
    @Autowired
    public final ProductRepo productRepo;

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable Long productId) {
        Product product = productService.getProduct(productId);

        if(product.getProduct_id() > 0) return new ResponseEntity<>(product, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/addproduct")
    public ResponseEntity<?> createProduct(@RequestBody Product product) throws IOException {
        Product savedProduct = null;
        savedProduct = productService.addProduct(product);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @PutMapping("/updateproduct/{productId}")
    public ResponseEntity<?> updateProduct(@RequestBody Product product, @PathVariable Long productId) throws IOException {
        Product savedProduct = null;
        savedProduct = productService.updateProduct(product, productId);
        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
    }

    @DeleteMapping("/deleteproduct/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        Product product = productService.getProduct(productId);
        if(product != null){
            productService.deleteProduct(productId);
            System.out.println("Deleted");
            return new ResponseEntity<>("Deleted", HttpStatus.OK);
        }
        else{
            System.out.println("Deletion Failed");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}