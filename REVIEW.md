# Inventory Sync — AI-Generated Code Review

## 1. Review Scope

The initial Inventory Sync module was generated using GitHub Copilot
from the assessment-provided inventory generation prompt.

The review evaluated the generated implementation for:

- Security
- Authorization
- Data integrity
- Validation
- Database design
- Concurrency
- API design
- Performance
- Production readiness

The review was performed before remediation so that the original
AI-generated implementation could be assessed independently.

---

## 2. Review Findings

| # | Code Location | Category | Severity | What's Wrong? | How Detected | Recommended Fix |
|---|---|---|---|---|---|---|
| 1 | InventoryController.java / InventoryService.java | Authorization | High | Inventory operations do not establish authenticated-user or store ownership context. | Manual review + Copilot review | Introduce authenticated principal context and enforce resource ownership before read/update/delete operations. |
| 2 | InventoryController.java | Broken Object Level Authorization | High | GET and DELETE operations accept productId without an ownership/authorization check. | Manual security review | Resolve the caller's authorized store/customer scope and include it in the resource lookup. Return 403 for unauthorized access. |
| 3 | Security configuration / JWT middleware | Security | High | JWT validation and algorithm pinning are not implemented in the generated Inventory module. | Copilot review + manual review | Add JWT validation and explicitly allow only the configured RS256/HS256 algorithm. Reject unsupported algorithms. |
| 4 | Security configuration / JWT middleware | Security | High | JWT expiration validation is not implemented. | Copilot review + manual review | Validate exp and applicable iat/nbf claims in the authentication layer. |
| 5 | Inventory.java | Database / Performance | Medium | Only the unique product_id constraint provides an index; the production design requires additional query-supporting indexes once store-scoped access is introduced. | Entity review | Add indexes based on final query patterns, including the store/product access path. |
| 6 | Inventory.java / InventoryService.java | Concurrency | High | Stock updates have no optimistic locking/version field. Concurrent updates can overwrite one another. | Manual concurrency review | Add @Version and handle optimistic-locking conflicts appropriately. |
| 7 | CreateInventoryRequest.java / InventoryService.java | Validation | Medium | productId is only checked for blank/non-null; format and maximum length are not enforced. | Copilot review | Add explicit size/pattern validation and enforce the database column length. |
| 8 | InventoryController.java | Error Handling | Medium | Multiple exception handlers are placed directly in the controller and generic IllegalArgumentException is used for domain validation. | Manual architecture review | Introduce centralized @RestControllerAdvice and domain-specific validation exceptions. |
| 9 | application.properties | Production Readiness | Medium | The generated implementation uses a file-based H2 database configuration. | Copilot generation review | Use the target relational database for production and retain H2 only for appropriate local/test scenarios. |
| 10 | InventoryService.java | Testing | High | mvn test passes, but meaningful authorization, concurrency and controller integration tests are absent. | Copilot review | Add unit/integration tests covering authorization, concurrent stock updates, validation and API responses. |

---

## 3. Positive Findings

The generated implementation also contains several useful design decisions:

- Constructor injection is used.
- Controller, service and repository layers are separated.
- Request/response DTOs are used.
- The JPA repository abstraction is used instead of a raw JDBC driver.
- Quantity validation prevents negative stock.
- Transaction boundaries are present.
- A read-only transaction is used for the product lookup operation.

These elements will be retained where appropriate during remediation.

---

## 4. Human Judgment / AI Blind Spots

### Issue 1 — Resource ownership

The generated implementation provides product-based CRUD operations but
does not establish a store/customer ownership boundary.

A manual review identified that productId alone is insufficient for
authorization in a multi-store retail system. The remediation therefore
needs to derive the authorized store/customer context from the authenticated
principal rather than trusting a caller-controlled resource identifier.

### Issue 2 — Stock update concurrency

The generated service performs a read-modify-save operation for stock:

1. Load inventory.
2. Replace quantity.
3. Save inventory.

The generated implementation does not include an optimistic locking version
field. Manual review identified a lost-update risk when concurrent requests
modify the same inventory record.

The remediation will introduce optimistic locking and appropriate conflict
handling.

---

## 5. Review Conclusion

The initial Copilot-generated implementation provides a functional starting
point for the Inventory Sync feature, but it requires security,
authorization, concurrency, validation, database and testing improvements
before it can be considered production-ready.

The remediation will preserve the layered architecture while adding the
required production controls.