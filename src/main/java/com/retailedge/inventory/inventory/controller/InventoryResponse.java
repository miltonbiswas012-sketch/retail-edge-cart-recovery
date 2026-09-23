package com.retailedge.inventory.inventory.controller;

import com.retailedge.inventory.inventory.model.Inventory;

public record InventoryResponse(Long id, String productId, int quantity, long version) {

    public static InventoryResponse from(Inventory inventory) {
        return new InventoryResponse(
                inventory.getId(), inventory.getProductId(), inventory.getQuantity(), inventory.getVersion());
    }
}
