# OrderDesk

Order service for the online bookstore: REST orders, validation, stock reservation, pricing with
coupons and tax, confirmations. Java 21, Spring Boot 4.1.2.

## Build and test

- Run one test class: `mvn -q test -Dtest=<ClassName>`
- Run the full suite before finishing: `mvn -q test`
- Run the app: `mvn spring-boot:run` (port 8080)

## Where things are

- `order`: OrderValidator (input rules), OrderService, OrderController, OrderRepository
- `pricing`: PriceCalculator, DiscountPolicy, TaxCalculator
- `inventory`, `notification`, `reporting`, `common` (ValidationException, ApiExceptionHandler)

## Rules

- Money is BigDecimal with scale 2 and RoundingMode.HALF_UP; compare with compareTo.
- Bad client input throws ValidationException; ApiExceptionHandler maps it to 400.
- Constructor injection, final fields, records for data carriers.
- Don't change test expectations to make a test pass; fix the code.
- Never log customer personal data.

## More guidance (read only when the task needs it)

- REST API catalogue: docs/api.md
- Coding style details: docs/style.md
- Release, environments and incident runbook: the orderdesk-release skill
