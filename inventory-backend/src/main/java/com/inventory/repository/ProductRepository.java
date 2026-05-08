package com.inventory.repository;

import com.inventory.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByNameContainingIgnoreCase(String keyword);

    List<Product> findByCategoryIgnoreCase(String category);

    List<Product> findByOrderByPriceAsc();

    List<Product> findByOrderByPriceDesc();

    Product findByName(String name);

    List<Product> findByStatus(String status);

    // Returns all distinct category names (used for dynamic filter dropdowns)
    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findDistinctCategories();
}
