package com.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.inventory.entity.Product;
import com.inventory.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    // CREATE / UPDATE
    public Product save(Product product) {

        //  Logic: Low Stock Detection
        if (product.getQuantity() < 10) {
            product.setStatus("LOW_STOCK");
        } else {
            product.setStatus("AVAILABLE");
        }

        return repo.save(product);
    }

    // READ ALL
    public List<Product> getAll() {
        return repo.findAll();
    }

    // READ BY ID
    public Product getById(int id) {
        return repo.findById(id).orElse(null);
    }

    // DELETE
    public void delete(int id) {
        repo.deleteById(id);
    }

    // SEARCH
    public List<Product> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // FILTER BY CATEGORY
    public List<Product> filterByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    // SORT BY PRICE ASC
    public List<Product> sortByPriceAsc() {
        return repo.findByOrderByPriceAsc();
    }

    // SORT BY PRICE DESC
    public List<Product> sortByPriceDesc() {
        return repo.findByOrderByPriceDesc();
    }
}