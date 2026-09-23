package com.retailedge.inventory.inventory.controller;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record UpdateStockRequest(@NotNull @PositiveOrZero Integer quantity) {
}
