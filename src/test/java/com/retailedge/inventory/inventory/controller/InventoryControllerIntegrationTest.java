package com.retailedge.inventory.inventory.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void inventoryRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/api/inventory/product/sku-1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void invalidCreateRequestIsRejected() throws Exception {
        mockMvc.perform(post("/api/inventory")
                        .with(jwt().jwt(token -> token.claim("store_id", "store-a")))
                        .contentType("application/json")
                        .content("{\"productId\":\"sku-1\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticatedStoreCanCreateAndReadItsInventory() throws Exception {
        mockMvc.perform(post("/api/inventory")
                        .with(jwt().jwt(token -> token.claim("store_id", "store-a")))
                        .contentType("application/json")
                        .content("{\"productId\":\"sku-integration\",\"quantity\":4}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/inventory/product/sku-integration")
                        .with(jwt().jwt(token -> token.claim("store_id", "store-a"))))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/inventory/product/sku-integration")
                        .with(jwt().jwt(token -> token.claim("store_id", "store-b"))))
                .andExpect(status().isNotFound());
    }
}
