package com.ayush.ecommerce.module.product.repository;

import com.ayush.ecommerce.module.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByIdAndActiveTrue(Long id);

    Page<Product> findByActiveTrue(Pageable pageable);

    List<Product> findByCategoryIdAndActiveTrue(Long categoryId);

    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String keyword);

    List<Product> findByIdInAndActiveTrue(List<Long> ids);

    List<Product> findByPriceBetweenAndActiveTrue(
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

    List<Product> findByCategoryIdAndPriceBetweenAndActiveTrue(
            Long categoryId,
            BigDecimal minPrice,
            BigDecimal maxPrice
    );

}
