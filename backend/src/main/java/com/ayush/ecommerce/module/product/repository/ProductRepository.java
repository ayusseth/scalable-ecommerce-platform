package com.ayush.ecommerce.module.product.repository;

import com.ayush.ecommerce.module.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndActiveTrue(Long id);

    Page<Product> findByActiveTrue(Pageable pageable);

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String keyword);
}
