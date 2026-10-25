# OrderDesk Java style


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

