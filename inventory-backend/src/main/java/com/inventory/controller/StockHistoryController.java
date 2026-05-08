package com.inventory.controller;

import com.inventory.entity.StockHistory;
import com.inventory.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/stock-history")
public class StockHistoryController {

    @Autowired
    private ProductService service;

    // GET all stock history, newest first
    @GetMapping
    public List<StockHistory> getAll() {
        return service.getAllHistory();
    }
}
