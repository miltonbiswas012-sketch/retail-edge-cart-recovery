package com.retailedge.inventory.inventory.service;

public class InventoryAlreadyExistsException extends RuntimeException {

    public InventoryAlreadyExistsException(String productId) {
        super("Inventory already exists for product: " + productId);
    }
}
