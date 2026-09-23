# Pull Request Description

## Title

Implement secure inventory sync and cart abandonment recovery

## Summary

This change turns the initial AI-generated inventory module into a security- and data-integrity-aware implementation and adds the RetailEdge cart abandonment recovery workflow.

## Inventory remediation

- Store-scoped authorization from JWT.
- RS256 JWT algorithm pinning.
- Issuer/time validation.
- Request validation.
- Centralized ProblemDetail error handling.
- PostgreSQL + Flyway production configuration.
- Store/product indexes.
- Optimistic locking.
- Security, BOLA, validation, concurrency and integration tests.

## Cart recovery

- 30-minute inactivity threshold.
- Configurable batch size.
- Configurable voucher threshold.
- Recovery event followed by notification.
- 24-hour recovery window.
- Conversion and expiry transitions.
- Customer ownership checks.
- Cursor-based pagination.
- BigDecimal monetary values.

## Verification

Run:

```bash
mvn clean test
```

The final evidence should show zero test failures/errors and include screenshots of the source, tests and successful Maven build.
