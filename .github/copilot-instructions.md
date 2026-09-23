# RetailEdge Engineering Standards

## Technology Stack

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL for production
- Maven
- JUnit 5
- Mockito

## Architecture

Use the following layered architecture:

Controller -> Service -> Repository -> Database

Controllers must handle HTTP concerns only.

Business logic belongs in the service layer.

Persistence logic belongs in repositories.

Do not use raw JDBC or database drivers directly in business logic.

## API Standards

Use request and response DTOs.

Never bind HTTP request bodies directly to JPA entities.

Only explicitly exposed DTO fields may be written.

Validate all external inputs.

Use meaningful HTTP status codes.

Use centralized exception handling with @RestControllerAdvice.

## Retail Data Rules

Use BigDecimal for all monetary values.

Never use float or double for currency.

Use explicit database precision and scale for monetary columns.

Persist timestamps using UTC-aware types such as Instant.

Inventory quantities must never become negative.

## Security

JWT authentication must explicitly pin the accepted signing algorithm
to the configured RS256 or HS256 algorithm.

Never accept the JWT "none" algorithm.

Validate JWT expiration.

Validate applicable iat and nbf claims.

Do not log JWTs, credentials, secrets, or sensitive personal data.

## Authorization

Every resource-fetch operation must verify that the authenticated
principal is authorized to access the requested resource.

Protect against Broken Object Level Authorization.

Never trust a caller-provided customerId or storeId as proof of ownership.

Derive authorization scope from the authenticated security context.

Unauthorized resource access must return 403 where appropriate.

## Pagination

Large collection endpoints must use cursor-based pagination.

Do not use OFFSET pagination for production collection APIs.

Use a stable cursor based on deterministic ordering.

Collection APIs must accept a configurable limit.

Responses should provide the next cursor when additional records exist.

## Database

Use Spring Data JPA.

Use appropriate database indexes for query patterns.

At least two meaningful index declarations must exist in the final
production model.

Use optimistic locking where concurrent updates can occur.

## Validation

Use Jakarta Bean Validation for request validation.

Use @NotBlank for required strings.

Use @Size or @Pattern where appropriate.

Use @PositiveOrZero for quantities where zero is valid.

Do not rely only on service-level validation.

## Error Handling

Use domain-specific exceptions.

Do not use catch(Exception) as a generic error handler.

Use @RestControllerAdvice for centralized API error responses.

Do not expose stack traces or internal implementation details.

## Logging

Use structured logging.

Do not log passwords, JWTs, credentials, secrets, payment information,
or unnecessary personally identifiable information.

## Testing

Unit tests must cover business rules.

Integration tests must cover REST endpoints.

Security tests must verify unauthorized access.

BOLA tests must verify that one user cannot access another user's resource.

Test validation failures.

Test concurrent inventory update behavior where applicable.

## Copilot Usage

Review all AI-generated code before accepting it.

AI-generated code must not be considered production-ready without
human review and testing.

Document important Copilot prompts and post-generation corrections.