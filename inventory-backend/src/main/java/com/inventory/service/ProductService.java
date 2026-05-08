package com.inventory.service;

import com.inventory.entity.Product;
import com.inventory.entity.StockHistory;
import com.inventory.exception.DuplicateProductException;
import com.inventory.exception.InvalidStockException;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.repository.ProductRepository;
import com.inventory.repository.StockHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private StockHistoryRepository historyRepo;

    // ─── CREATE ──────────────────────────────────────────────────────────────

    public Product create(Product product, String changedBy) {

        validateProduct(product, -1);

        applyStatus(product);

        Product saved = repo.save(product);

        recordHistory(saved.getName(), "ADDED", null, saved.getQuantity(), changedBy);

        return saved;
    }

    // ─── UPDATE ──────────────────────────────────────────────────────────────

    public Product update(int id, Product incoming, String changedBy) {

        Product existing = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));

        validateProduct(incoming, id);

        int oldQty = existing.getQuantity();

        existing.setName(incoming.getName());
        existing.setCategory(incoming.getCategory());
        existing.setPrice(incoming.getPrice());
        existing.setQuantity(incoming.getQuantity());
        existing.setLowStockThreshold(incoming.getLowStockThreshold() > 0
                ? incoming.getLowStockThreshold() : 10);

        applyStatus(existing);

        Product saved = repo.save(existing);

        // Only log if quantity actually changed
        if (oldQty != saved.getQuantity()) {
            recordHistory(saved.getName(), "UPDATED", oldQty, saved.getQuantity(), changedBy);
        }

        return saved;
    }

    // ─── READ ALL ─────────────────────────────────────────────────────────────

    public List<Product> getAll() {
        return repo.findAll();
    }

    // ─── READ BY ID ───────────────────────────────────────────────────────────

    public Product getById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with ID " + id + " not found"));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    public void delete(int id, String changedBy) {
        Product product = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(
                        "Cannot delete. Product with ID " + id + " not found"));

        recordHistory(product.getName(), "DELETED", product.getQuantity(), null, changedBy);
        repo.delete(product);
    }

    // ─── SEARCH ───────────────────────────────────────────────────────────────

    public List<Product> search(String keyword) {
        return repo.findByNameContainingIgnoreCase(keyword);
    }

    // ─── FILTER BY CATEGORY ───────────────────────────────────────────────────

    public List<Product> filterByCategory(String category) {
        return repo.findByCategoryIgnoreCase(category);
    }

    // ─── FILTER BY STATUS ─────────────────────────────────────────────────────

    public List<Product> filterByStatus(String status) {
        return repo.findByStatus(status.toUpperCase());
    }

    // ─── SORT ─────────────────────────────────────────────────────────────────

    public List<Product> sortByPriceAsc() {
        return repo.findByOrderByPriceAsc();
    }

    public List<Product> sortByPriceDesc() {
        return repo.findByOrderByPriceDesc();
    }

    // ─── DISTINCT CATEGORIES ─────────────────────────────────────────────────

    public List<String> getCategories() {
        return repo.findDistinctCategories();
    }

    // ─── STOCK HISTORY ────────────────────────────────────────────────────────

    public List<StockHistory> getAllHistory() {
        return historyRepo.findAllByOrderByChangedAtDesc();
    }

    // ─── PRIVATE HELPERS ─────────────────────────────────────────────────────

    private void validateProduct(Product product, int excludeId) {

        if (product.getQuantity() < 0) {
            throw new InvalidStockException("Product quantity cannot be negative");
        }
        if (product.getPrice() < 0) {
            throw new InvalidStockException("Product price cannot be negative");
        }

        Product existing = repo.findByName(product.getName());
        if (existing != null && existing.getId() != excludeId) {
            throw new DuplicateProductException(
                    "Product with name '" + product.getName() + "' already exists");
        }
    }

    private void applyStatus(Product product) {
        int threshold = product.getLowStockThreshold() > 0 ? product.getLowStockThreshold() : 10;
        product.setStatus(product.getQuantity() < threshold ? "LOW_STOCK" : "AVAILABLE");
    }

    private void recordHistory(String productName, String changeType,
                               Integer oldQty, Integer newQty, String changedBy) {
        StockHistory history = new StockHistory();
        history.setProductName(productName);
        history.setChangeType(changeType);
        history.setOldQuantity(oldQty);
        history.setNewQuantity(newQty);
        history.setChangedBy(changedBy != null ? changedBy : "system");
        historyRepo.save(history);
    }
}
