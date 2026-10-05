Source code for the article https://howtodoinjava.com/jackson/java-8-date-time-type-not-supported-by-default/

# Jackson error: Java 8 date/time type not supported by default

A small Maven project that reproduces the error with a bare Jackson 2 `ObjectMapper`,
fixes it with `JavaTimeModule`, shows what `MapperFeature.REQUIRE_HANDLERS_FOR_JAVA8_TIMES`
does, and shows that Jackson 3 (`tools.jackson`) needs no module at all.

## Versions

- Java 25 (Temurin 25.0.4.1), Maven 3.9.16
- Jackson 2.22.3 (jackson-databind, jackson-datatype-jsr310)
- Jackson 3.2.3 (tools.jackson.core:jackson-databind)
- JUnit 6.1.3 (JUnit Jupiter)
- maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0

Both Jackson versions live in one project because Jackson 3 uses the package and
group id `tools.jackson`, while Jackson 2 keeps `com.fasterxml.jackson`.

## Run

```bash
mvn test
```

Run one test class:

```bash
mvn test -Dtest=BareMapperErrorTest
```

The tests print the error messages and the JSON output to the console, so the build stays green.

## Contents

| File | What it shows |
|---|---|
| `Reminder` | A record with `LocalDate`, `LocalDateTime` and `Instant` fields |
| `FormattedReminder` | The same record with `@JsonFormat(pattern = ...)` on the date fields |
| `JavaTimeMappers` | A bare mapper, `registerModule()`, `findAndRegisterModules()` and the `JsonMapper.builder()` style |
| `BareMapperErrorTest` | Reproduces the error for `writeValueAsString()`, `readValue()` and `convertValue()` |
| `JavaTimeModuleFixTest` | The fix, the default array/timestamp output, `WRITE_DATES_AS_TIMESTAMPS` and `@JsonFormat` |
| `RequireHandlersFeatureTest` | What happens when `REQUIRE_HANDLERS_FOR_JAVA8_TIMES` is disabled |
| `Jackson3BuiltInTest` | Jackson 3 writes and reads `java.time` values without any module |
