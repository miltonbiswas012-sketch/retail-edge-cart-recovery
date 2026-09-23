package com.retailedge.inventory.inventory.service;

public class InventoryValidationException extends RuntimeException {

    public InventoryValidationException(String message) {
        super(message);
    }
}
