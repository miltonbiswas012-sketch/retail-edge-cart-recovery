package com.retailedge.inventory.inventory.controller;

import com.retailedge.inventory.inventory.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }
    @PostMapping
    public ResponseEntity<InventoryResponse> create(@Valid @RequestBody CreateInventoryRequest request) {
        InventoryResponse response = InventoryResponse.from(
                inventoryService.create(request.productId(), request.quantity()));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PatchMapping("/{productId}/stock")
    public InventoryResponse updateStock(
            @PathVariable @Size(max = 128) String productId,
            @Valid @RequestBody UpdateStockRequest request) {
        return InventoryResponse.from(inventoryService.updateStock(productId, request.quantity()));
    }
    @GetMapping("/product/{productId}")
    public InventoryResponse getByProduct(@PathVariable @Size(max = 128) String productId) {
        return InventoryResponse.from(inventoryService.getByProduct(productId));
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> delete(@PathVariable @Size(max = 128) String productId) {
        inventoryService.delete(productId);
        return ResponseEntity.noContent().build();
    }
}
