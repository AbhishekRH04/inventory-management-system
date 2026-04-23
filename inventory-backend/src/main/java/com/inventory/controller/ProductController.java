package com.inventory.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.inventory.entity.Product;
import com.inventory.service.ProductService;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;

    // CREATE
    @PostMapping
    public Product add(@RequestBody Product p) {
        return service.save(p);
    }

    // READ ALL
    @GetMapping
    public List<Product> getAll() {
        return service.getAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public Product getOne(@PathVariable int id) {
        return service.getById(id);
    }

    // UPDATE
    @PutMapping("/{id}")
    public Product update(@PathVariable int id, @RequestBody Product p) {
        p.setId(id);
        return service.save(p);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable int id) {
        service.delete(id);
        return "Deleted Successfully";
    }

    // SEARCH
    @GetMapping("/search")
    public List<Product> search(@RequestParam String keyword) {
        return service.search(keyword);
    }

    // FILTER
    @GetMapping("/category")
    public List<Product> filter(@RequestParam String category) {
        return service.filterByCategory(category);
    }

    // SORT ASC
    @GetMapping("/sort/asc")
    public List<Product> sortAsc() {
        return service.sortByPriceAsc();
    }

    // SORT DESC
    @GetMapping("/sort/desc")
    public List<Product> sortDesc() {
        return service.sortByPriceDesc();
    }
}