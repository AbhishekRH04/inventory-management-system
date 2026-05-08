package com.inventory.controller;

import com.inventory.entity.Product;
import com.inventory.entity.StockHistory;
import com.inventory.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // ─── CREATE ──────────────────────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<Product> add(
            @Valid @RequestBody Product p,
            @RequestHeader(value = "X-Username", defaultValue = "anonymous") String username) {

        return ResponseEntity.ok(service.create(p, username));
    }

    // ─── READ ALL ─────────────────────────────────────────────────────────────

    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    // ─── READ BY ID ───────────────────────────────────────────────────────────

    @GetMapping("/{id}")
    public Product getOne(@PathVariable int id) {
        return service.getById(id);
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<Product> update(
            @PathVariable int id,
            @Valid @RequestBody Product p,
            @RequestHeader(value = "X-Username", defaultValue = "anonymous") String username) {

        return ResponseEntity.ok(service.update(id, p, username));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable int id,
            @RequestHeader(value = "X-Username", defaultValue = "anonymous") String username) {

        service.delete(id, username);
        return ResponseEntity.ok("Product deleted successfully");
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    // ─── FILTER BY CATEGORY ───────────────────────────────────────────────────

    @GetMapping("/category")
    public List<Product> filterByCategory(@RequestParam String category) {
        return service.filterByCategory(category);
    }

    // ─── FILTER BY STATUS ─────────────────────────────────────────────────────

    @GetMapping("/status")
    public List<Product> filterByStatus(@RequestParam String status) {
        return service.filterByStatus(status);
    }

    // ─── DISTINCT CATEGORIES ─────────────────────────────────────────────────

    @GetMapping("/categories")
    public List<String> getCategories() {
        return service.getCategories();
    }

    // ─── SORT ASC ─────────────────────────────────────────────────────────────

    @GetMapping("/sort/asc")
    public List<Product> sortAsc() {
        return service.sortByPriceAsc();
    }

    // ─── SORT DESC ────────────────────────────────────────────────────────────

    @GetMapping("/sort/desc")
    public List<Product> sortDesc() {
        return service.sortByPriceDesc();
    }
}
