package com.retailedge.inventory.inventory.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.retailedge.inventory.inventory.model.Inventory;
import com.retailedge.inventory.inventory.repository.InventoryRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private AuthenticatedStoreScope authenticatedStoreScope;

    @InjectMocks
    private InventoryService inventoryService;

    @Test
    void createUsesAuthenticatedStoreScope() {
        when(authenticatedStoreScope.requireStoreId()).thenReturn("store-a");
        when(inventoryRepository.findByStoreIdAndProductId("store-a", "sku-1"))
                .thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        inventoryService.create("sku-1", 5);

        verify(inventoryRepository).findByStoreIdAndProductId("store-a", "sku-1");
    }

    @Test
    void anotherStoreCannotReadInventoryByProductId() {
        when(authenticatedStoreScope.requireStoreId()).thenReturn("store-b");
        when(inventoryRepository.findByStoreIdAndProductId("store-b", "sku-1"))
                .thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class,
                () -> inventoryService.getByProduct("sku-1"));
        verify(inventoryRepository).findByStoreIdAndProductId("store-b", "sku-1");
    }

    @Test
    void concurrentUpdateBecomesDomainConflict() {
        Inventory inventory = new Inventory("store-a", "sku-1", 5);
        when(authenticatedStoreScope.requireStoreId()).thenReturn("store-a");
        when(inventoryRepository.findByStoreIdAndProductId("store-a", "sku-1"))
                .thenReturn(Optional.of(inventory));
        when(inventoryRepository.save(inventory))
                .thenThrow(new ObjectOptimisticLockingFailureException(Inventory.class, 1L));

        assertThrows(InventoryConcurrencyException.class,
                () -> inventoryService.updateStock("sku-1", 8));
    }

    @Test
    void negativeQuantityIsRejectedAsDomainValidation() {
        assertThrows(InventoryValidationException.class,
                () -> inventoryService.create("sku-1", -1));
    }
}
