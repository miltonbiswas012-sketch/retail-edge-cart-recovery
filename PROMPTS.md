# Copilot Prompts and Human Corrections

## 1. Initial generation prompt

> Generate an inventory model and an inventory service with create, update-stock, get-by-product, and delete functions. Use a database.

This prompt intentionally represents the original narrow generation task.

## 2. Security review prompt

> Review the generated inventory implementation for authentication, authorization, BOLA, JWT validation, input validation, database safety, concurrency, error handling, and production readiness. Identify concrete defects and propose remediations.

## 3. Human review corrections

The human review identified that productId alone cannot establish ownership in a multi-store system. The implementation was changed to derive store scope from the authenticated JWT claim `store_id`.

The review also identified lost-update risk in read-modify-write stock updates. JPA optimistic locking with `@Version` and conflict mapping to HTTP 409 was added.

Additional corrections included:
- Jakarta Bean Validation on request DTOs and path variables.
- PostgreSQL production configuration with environment variables.
- Flyway schema migration.
- Centralized `@RestControllerAdvice`.
- Security/concurrency/controller integration tests.
- RS256 algorithm pinning and standard issuer/time validation.
- Store/product database indexes.

## 4. Cart recovery implementation prompt

> Extend RetailEdge with cart abandonment recovery. Treat a cart as abandoned after at least 30 minutes of inactivity. Process abandoned carts in a configurable batch, apply a configurable voucher threshold, emit a recovery event and notification, provide a 24-hour recovery window, support conversion/expiry, enforce owner authorization, and expose cursor-based pagination.

The implementation was then manually reviewed for boundary conditions, BOLA, money precision, deterministic pagination, concurrency, and test coverage.
