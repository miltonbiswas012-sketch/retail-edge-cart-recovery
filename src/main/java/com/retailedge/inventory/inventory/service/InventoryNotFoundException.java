package com.retailedge.inventory.inventory.service;

public class InventoryNotFoundException extends RuntimeException {

    public InventoryNotFoundException(String productId) {
        super("No inventory exists for product: " + productId);
    }
}
