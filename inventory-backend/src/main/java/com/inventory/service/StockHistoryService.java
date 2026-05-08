package com.inventory.service;

import com.inventory.entity.StockHistory;
import com.inventory.repository.StockHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockHistoryService {

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    // GET all stock history
    public List<StockHistory> getAllHistory() {
        return stockHistoryRepository.findAllByOrderByChangedAtDesc();
    }

    // SEARCH history by product name
    public List<StockHistory> searchHistory(String productName) {
        return stockHistoryRepository
                .findByProductNameContainingIgnoreCaseOrderByChangedAtDesc(productName);
    }

    // SAVE history record
    public StockHistory saveHistory(StockHistory history) {
        return stockHistoryRepository.save(history);
    }
}