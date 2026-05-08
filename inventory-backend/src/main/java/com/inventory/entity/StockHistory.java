package com.inventory.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class StockHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;

    // ADDED, UPDATED, DELETED
    private String changeType;

    private Integer oldQuantity;
    private Integer newQuantity;

    private String changedBy;

    private LocalDateTime changedAt;

    @PrePersist
    public void onCreate() {
        this.changedAt = LocalDateTime.now();
    }

    // ─── Getters & Setters ───────────────────────────────────────────────────

    public Long getId() { return id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getChangeType() { return changeType; }
    public void setChangeType(String changeType) { this.changeType = changeType; }

    public Integer getOldQuantity() { return oldQuantity; }
    public void setOldQuantity(Integer oldQuantity) { this.oldQuantity = oldQuantity; }

    public Integer getNewQuantity() { return newQuantity; }
    public void setNewQuantity(Integer newQuantity) { this.newQuantity = newQuantity; }

    public String getChangedBy() { return changedBy; }
    public void setChangedBy(String changedBy) { this.changedBy = changedBy; }

    public LocalDateTime getChangedAt() { return changedAt; }
}
