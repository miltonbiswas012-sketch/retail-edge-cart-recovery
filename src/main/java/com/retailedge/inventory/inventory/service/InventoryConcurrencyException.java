package com.retailedge.inventory.inventory.service;

public class InventoryConcurrencyException extends RuntimeException {

    public InventoryConcurrencyException() {
        super("The inventory changed concurrently; retry the request");
    }
}
