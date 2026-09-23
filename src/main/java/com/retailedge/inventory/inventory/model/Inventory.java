package com.retailedge.inventory.inventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.Instant;

@Entity
@Table(name = "inventory", indexes = {
    @Index(name = "uk_inventory_store_product", columnList = "store_id, product_id", unique = true),
    @Index(name = "idx_inventory_store", columnList = "store_id")
})
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false, length = 64)
    private String storeId;

    @Column(name = "product_id", nullable = false, length = 128)
    private String productId;

    @Column(nullable = false)
    private int quantity;

    @Version
    @Column(nullable = false)
    private long version;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Inventory() {
    }

    public Inventory(String storeId, String productId, int quantity) {
        this.storeId = storeId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getVersion() {
        return version;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void updateQuantity(int quantity) {
        this.quantity = quantity;
    }

    @PrePersist
    @PreUpdate
    private void updateTimestamp() {
        updatedAt = Instant.now();
    }
}
