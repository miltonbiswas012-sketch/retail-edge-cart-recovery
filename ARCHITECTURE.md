# RetailEdge Architecture

## Overview

RetailEdge contains two related bounded areas:

1. Inventory Sync — the original AI-generated assessment feature and its security remediation.
2. Cart Abandonment Recovery — the assessment's business workflow extension.

## Layers

```
HTTP Controller
      |
      v
Application Service
      |
      +---- Domain events ----> Notification listener
      |
      v
Spring Data Repository
      |
      v
PostgreSQL
```

## Inventory flow

JWT -> Spring Security -> `AuthenticatedStoreScope` -> service -> store-scoped repository query -> database.

The caller does not supply a trusted storeId. The service derives store scope from the authenticated JWT `store_id` claim.

## Cart recovery flow

Cart activity -> inactivity >= 30 minutes -> batch recovery processor -> status ABANDONED -> RECOVERY_TRIGGERED -> recovery event -> notification -> 24-hour recovery window -> CONVERTED or EXPIRED.

## Security boundaries

- All business APIs require authentication.
- Cart resources are checked against the authenticated `customer_id` claim.
- Non-owner access is rejected with HTTP 403.
- Inventory uses store scope from the security context.
- JWT validation pins RS256 and uses issuer/time validation.

## Data integrity

- Monetary values use `BigDecimal`.
- Timestamps use `Instant`.
- Inventory uses optimistic locking.
- Cart uses optimistic locking for state transitions.
- Inventory and cart schemas have database constraints/indexes.
- Cursor pagination uses deterministic `createdAt + id` ordering.

## Eventing

Recovery is represented as an application event. The event is published after the cart state is changed to `RECOVERY_TRIGGERED`; a listener invokes the notification service. The notification implementation is intentionally replaceable by a real queue/email/SMS provider.

## Consistency

Recovery state changes are transactional. Notification delivery is treated as an external side effect and should be made idempotent in a production distributed deployment.
