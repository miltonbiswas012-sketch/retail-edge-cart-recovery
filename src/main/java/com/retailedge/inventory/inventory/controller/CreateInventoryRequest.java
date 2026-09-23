package com.retailedge.inventory.inventory.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record CreateInventoryRequest(
        @NotBlank @Size(max = 128) String productId,
        @PositiveOrZero Integer quantity) {
}
