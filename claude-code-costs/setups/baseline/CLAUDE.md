# OrderDesk project guide

OrderDesk is the order service for our online bookstore. It takes orders over REST, validates them,
reserves stock, prices them with coupons and tax, and sends a confirmation.

## Build and test

- Build: `mvn package`
- Run all tests: `mvn test`
- Run the app: `mvn spring-boot:run` (port 8080)
- Java 21, Spring Boot 4.1.2

## Package layout

- `com.howtodoinjava.orderdesk.order`: orders, validation, REST controller, in-memory repository
- `com.howtodoinjava.orderdesk.pricing`: subtotal, coupons and tax
- `com.howtodoinjava.orderdesk.inventory`: stock reservations
- `com.howtodoinjava.orderdesk.notification`: customer confirmations
- `com.howtodoinjava.orderdesk.reporting`: daily sales figures
- `com.howtodoinjava.orderdesk.common`: shared exceptions and the API error handler

## Java coding style

1. Use 4 spaces for indentation, never tabs.
2. Keep lines under 100 characters.
3. Use records for immutable data carriers such as requests and responses.
4. Use constructor injection; never field injection with @Autowired.
5. Mark injected fields final.
6. One public top-level class per file.
7. Order class members: constants, fields, constructors, public methods, private methods.
8. Name test methods after the behavior, such as rejectsNegativePrice.
9. Use BigDecimal for money; never double or float.
10. Scale money to 2 decimals with RoundingMode.HALF_UP.
11. Compare BigDecimal values with compareTo, not equals.
12. Log with SLF4J placeholders, not string concatenation.
13. Use DEBUG for per-line details and INFO for business events.
14. Never log customer email addresses or payment data.
15. Throw ValidationException for bad client input.
16. Let ApiExceptionHandler translate exceptions into HTTP responses.
17. Do not catch Exception in service code.
18. Prefer Optional for lookups that can miss.
19. Never return null from a public method.
20. Use List.of and Map.of for constant collections.
21. Keep methods under 30 lines where possible.
22. Extract a private method when a block needs a comment to explain it.
23. Avoid static state except constants.
24. Write Javadoc for public classes in the pricing package.
25. Imports: java, javax, then third-party, then com.howtodoinjava, separated by blank lines.
26. No wildcard imports.
27. Use var only when the type is obvious from the right-hand side.
28. Use text blocks for multi-line JSON in tests.
29. Prefer switch expressions over if chains on enums.
30. Keep controllers thin; business rules live in services.
31. Validate input at the service boundary, not in controllers.
32. Use package-private visibility for helpers used only in tests.
33. Do not add Lombok; we use records instead.
34. Use Clock injection for anything time-based.
35. Keep configuration in application.properties, not hard-coded values.
36. Use @ConfigurationProperties for groups of related settings.
37. Do not use @Value on more than two fields in one class.
38. Constants shared across packages go in the class that owns the rule.
39. Use meaningful names: total, subtotal, netAmount, not amt or tmp.
40. Tests use JUnit 5 and AssertJ-style readability, but plain Assertions are fine.

## REST API catalogue

### POST /orders

Create an order. Responses: 201 with the order, 400 for invalid input.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /orders/{id}

Read one order. Responses: 200 or 404.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /orders

List orders, paged with ?page and ?size. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /orders/{id}/confirm

Confirm a new order. Responses: 200, 409 if not NEW.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /orders/{id}/cancel

Cancel an order. Responses: 200, 409 if already shipped.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /orders/{id}/ship

Mark an order as shipped. Responses: 200, 409 if not CONFIRMED.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /orders/{id}/invoice

Download the invoice as PDF. Responses: 200 application/pdf.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /customers/{id}/orders

Orders for one customer. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /inventory/{sku}

Reserved and available stock for a SKU. Responses: 200 or 404.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /inventory/{sku}/restock

Add stock. Responses: 204.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /coupons

Active coupon codes. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### POST /coupons

Create a coupon. Responses: 201, 400 for duplicate code.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### DELETE /coupons/{code}

Retire a coupon. Responses: 204.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /reports/daily

Daily sales total. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /reports/top-skus

Top 10 SKUs this week. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

### GET /health

Liveness check. Responses: 200.

Example request:

```json
{"customerId":"c-1","couponCode":"WELCOME10","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}]}
```

Example response:

```json
{"id":1,"customerId":"c-1","lines":[{"sku":"BOOK","quantity":2,"unitPrice":12.50}],"total":24.30,"status":"NEW"}
```

## Release process

1. Create a release branch named release/x.y from main.
2. Update the version in pom.xml.
3. Run the full test suite and the smoke tests.
4. Write release notes in docs/releases/x.y.md with every user-visible change.
5. Open a pull request to main and get two approvals.
6. Tag the merge commit as vx.y.0.
7. The CI pipeline builds the container image and pushes it to the registry.
8. Deploy to staging with the deploy job and wait for health checks.
9. Run the staging smoke test suite against https://staging.orderdesk.example.
10. Ask the product owner to sign off in the release ticket.
11. Deploy to production in the 10:00 to 12:00 window, never on Fridays.
12. Watch the dashboard for 30 minutes after deploying.
13. If the error rate goes above 1 percent, roll back with the rollback job.
14. Announce the release in the team channel with a link to the notes.

## Deployment environments

| Environment | URL | Database | Notes |
|---|---|---|---|
| local | http://localhost:8080 | in-memory | default profile |
| dev | https://dev.orderdesk.example | postgres-dev | deployed on every merge |
| staging | https://staging.orderdesk.example | postgres-staging | copy of production schema |
| production | https://orderdesk.example | postgres-prod | two regions |

## Incident runbook

### Orders return 500

Check the logs for the exception, check the database connection pool, and roll back the last deploy if it started after a release.

### Totals are wrong

Compare the coupon table with the expected rates, check TaxCalculator.TAX_RATE, and recompute a sample order by hand.

### Stock goes negative

Look for concurrent reservations of the same SKU and check InventoryService logs for the order IDs.

### Confirmations not sent

Check the mail relay status page and NotificationService logs; resend from the admin screen.

### Slow responses

Check the p95 latency panel, the database slow query log, and recent traffic spikes.

### Health check failing

Check the container restarts, memory usage, and the last configuration change.

## Database migration notes (planned move from in-memory to PostgreSQL)

1. Use Flyway for schema changes; one migration per pull request.
2. Name migrations V<number>__<description>.sql.
3. Never edit a migration that has run in any shared environment.
4. Money columns use NUMERIC(12,2).
5. Every table has created_at and updated_at columns.
6. Add indexes for every foreign key.
7. Order lines live in order_lines with a foreign key to orders.
8. Coupons move to a coupons table with code, rate, valid_from, valid_to.
9. Inventory moves to a stock table keyed by sku.
10. Run migrations against a copy of staging before production.

## Code review checklist

- Tests cover the new behavior and the failure case.
- Money uses BigDecimal with scale 2.
- No secrets in code or properties files.
- Logs contain no personal data.
- Public API changes are reflected in this catalogue.
- Error responses use ApiExceptionHandler.
- New configuration has a sensible default.
- No commented-out code.
- Names say what things are.
- The pull request description explains why, not only what.
- Breaking changes are listed in the release notes.
- Performance-sensitive code has a benchmark or a measurement.

## Glossary

- SKU: stock keeping unit, the product code on an order line.
- Order line: one SKU with a quantity and a unit price.
- Subtotal: sum of quantity times unit price for all lines.
- Net amount: subtotal minus the coupon discount.
- Total: net amount plus tax.
- Reservation: stock held for an order until it ships or is cancelled.
- Coupon: a code that gives a percentage discount on the subtotal.

## Team contacts

- Orders and pricing: the checkout team channel
- Inventory: the warehouse systems team channel
- Release approvals: the product owner for checkout
