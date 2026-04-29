package com.inventory.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.inventory.entity.Product;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.exception.DuplicateProductException;
import com.inventory.exception.InvalidStockException;
import com.inventory.repository.ProductRepository;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    // CREATE / UPDATE
    public Product save(Product product) {

        // Duplicate product check
        Product existingProduct = repo.findByName(product.getName());

        if (existingProduct != null &&
                existingProduct.getId() != product.getId()) {

            throw new DuplicateProductException(
                    "Product with name '" + product.getName() + "' already exists"
            );
        }

        // Invalid stock validation
        if (product.getQuantity() < 0) {
            throw new InvalidStockException(
                    "Product quantity cannot be negative"
            );
        }

        // Low stock logic
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
        return repo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product with ID " + id + " not found"
                        )
                );
    }

    // DELETE
    public void delete(int id) {

        Product product = repo.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Cannot delete. Product with ID " + id + " not found"
                        )
                );

        repo.delete(product);
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