package com.ayush.ecommerce.module.product.repository;

import com.ayush.ecommerce.module.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
