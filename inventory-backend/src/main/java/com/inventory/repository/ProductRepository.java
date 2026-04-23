package com.inventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByOrderByPriceAsc();

    List<Product> findByOrderByPriceDesc();
}