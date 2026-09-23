package com.retailedge.inventory.inventory.service;

import com.retailedge.inventory.inventory.model.Inventory;
import com.retailedge.inventory.inventory.repository.InventoryRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final AuthenticatedStoreScope authenticatedStoreScope;

    public InventoryService(
            InventoryRepository inventoryRepository,
            AuthenticatedStoreScope authenticatedStoreScope) {
        this.inventoryRepository = inventoryRepository;
        this.authenticatedStoreScope = authenticatedStoreScope;
    }

    public Inventory create(String productId, Integer quantity) {
        validateProductId(productId);
        validateQuantity(quantity);
        String storeId = authenticatedStoreScope.requireStoreId();
        if (inventoryRepository.findByStoreIdAndProductId(storeId, productId).isPresent()) {
            throw new InventoryAlreadyExistsException(productId);
        }
        try {
            return inventoryRepository.save(new Inventory(storeId, productId, quantity));
        } catch (DataIntegrityViolationException exception) {
            throw new InventoryAlreadyExistsException(productId);
        }
    }

    public Inventory updateStock(String productId, Integer quantity) {
        validateProductId(productId);
        validateQuantity(quantity);
        Inventory inventory = getByProduct(productId);
        inventory.updateQuantity(quantity);
        try {
            return inventoryRepository.save(inventory);
        } catch (ObjectOptimisticLockingFailureException exception) {
            throw new InventoryConcurrencyException();
        }
    }

    @Transactional(readOnly = true)
    public Inventory getByProduct(String productId) {
        validateProductId(productId);
        String storeId = authenticatedStoreScope.requireStoreId();
        return inventoryRepository.findByStoreIdAndProductId(storeId, productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    public void delete(String productId) {
        validateProductId(productId);
        Inventory inventory = getByProduct(productId);
        inventoryRepository.delete(inventory);
    }

    private void validateProductId(String productId) {
        if (productId == null || productId.isBlank() || productId.length() > 128) {
            throw new InventoryValidationException("productId is invalid");
        }
    }

    private void validateQuantity(Integer quantity) {
        if (quantity == null || quantity < 0) {
            throw new InventoryValidationException("quantity is invalid");
        }
    }
}
