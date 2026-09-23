# Impact Analysis

## Functional impact

### Inventory

- Existing create/update/get/delete behavior remains available.
- Operations are now scoped to the authenticated store.
- Cross-store reads resolve as not-found to avoid disclosing resource existence.
- Concurrent stock updates produce a domain conflict instead of silently overwriting data.

### Cart recovery

- Carts become eligible for abandonment processing after 30 minutes of inactivity.
- Recovery processing is bounded by a configurable batch size.
- A configurable cart-total threshold controls voucher issuance.
- Recovery events and notifications are generated.
- Recovery expires after 24 hours unless converted.
- Cart listing is cursor paginated.

## API impact

New cart endpoints:
- `POST /api/v1/carts`
- `GET /api/v1/carts`
- `GET /api/v1/carts/{id}`
- `POST /api/v1/carts/{id}/touch`
- `POST /api/v1/carts/{id}/convert`

## Data impact

New cart and cart-item tables are introduced through Flyway. Monetary fields use decimal precision. Recovery timestamps and status are persisted.

## Security impact

The authenticated principal is the source of ownership scope. Caller-provided customer identifiers are not treated as proof of ownership.

## Operational impact

Recovery processing runs on a configurable scheduler. In a multi-instance deployment, a distributed scheduler/lock or queue-based worker should be used to prevent duplicate processing.

## Compatibility

The inventory API is preserved. The new cart API is additive.

## Test impact

The assessment requires coverage for timing, voucher threshold, events, notifications, 24-hour expiry, conversion, cursor pagination, authentication, and BOLA.
