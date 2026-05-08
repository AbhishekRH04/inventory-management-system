package com.inventory.repository;

import com.inventory.entity.StockHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockHistoryRepository extends JpaRepository<StockHistory, Long> {

    // All history for a specific product name
    List<StockHistory> findByProductNameContainingIgnoreCaseOrderByChangedAtDesc(String productName);

    // All history, newest first
    List<StockHistory> findAllByOrderByChangedAtDesc();
}
