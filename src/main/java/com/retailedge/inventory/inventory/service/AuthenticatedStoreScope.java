package com.retailedge.inventory.inventory.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticatedStoreScope {

    public String requireStoreId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof Jwt jwt)) {
            throw new InventoryAuthorizationException("Authenticated store scope is required");
        }
        String storeId = jwt.getClaimAsString("store_id");
        if (storeId == null || storeId.isBlank()) {
            throw new InventoryAuthorizationException("Authenticated store scope is required");
        }
        return storeId;
    }
}
