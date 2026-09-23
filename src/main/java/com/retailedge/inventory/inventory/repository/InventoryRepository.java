package com.retailedge.inventory.inventory.repository;

import com.retailedge.inventory.inventory.model.Inventory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByStoreIdAndProductId(String storeId, String productId);

    void deleteByStoreIdAndProductId(String storeId, String productId);
}
