# Tool Strategy

| Tool / Technique | Purpose | Evidence |
|---|---|---|
| GitHub Copilot Chat | Initial inventory generation and code review | Initial inventory module + review findings |
| Copilot instructions | Persistent engineering/security constraints | `.github/copilot-instructions.md` |
| IDE/manual review | Human validation of AI output | `REVIEW.md` blind-spot section |
| Maven test | Regression and integration verification | Test suite / build output |
| Spring Security Test | JWT-authenticated MockMvc requests | Security and BOLA tests |
| Mockito | Service-level business-rule isolation | Inventory/cart unit tests |
| Flyway | Reproducible relational schema | Versioned SQL migrations |
| JPA optimistic locking | Prevent lost updates | `@Version` + conflict test |

## Review method

1. Generate the narrow inventory implementation.
2. Review the generated code before treating it as production-ready.
3. Record security and data-integrity findings.
4. Remediate one concern at a time.
5. Add tests for each important defect.
6. Extend the project with cart recovery requirements.
7. Document architecture, impact, prompts, and acceptance criteria.
8. Run the full Maven test suite and preserve screenshot evidence.

## AI blind-spot focus

Human review is specifically required for:
- resource ownership boundaries;
- business timing rules;
- concurrent state transitions;
- monetary calculations;
- event ordering;
- information disclosure through error responses;
- pagination correctness;
- production configuration.
