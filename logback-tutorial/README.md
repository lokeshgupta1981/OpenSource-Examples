# Logback Tutorial Examples

Source code for the article [Logback Tutorial with logback.xml Configuration Examples](https://howtodoinjava.com/logback/logback-tutorial/).

A self-contained Maven module (it does not use the parent `opensource-examples` pom).
Tested with Logback 1.6.5 (logback-classic), SLF4J 2.0.20, JUnit 6.1.3 and Java 25.

## Files

- `LogbackDemo.java`: runs a small library example (borrow and return books) with one configuration file.
- `library/LoanService.java`, `library/CatalogService.java`: classes that log at DEBUG, INFO, WARN and ERROR.
- `src/main/resources/logback-examples/*.xml`: one configuration file per topic:
  `console`, `levels`, `additivity-duplicate`, `additivity-fixed`, `rolling`, `mdc`, `async`,
  `conditional`, `broken` (typo in an appender-ref) and `legacy-if` (old Janino-style condition).
- `LogbackConfigTest.java`: loads each file and checks the log lines (10 tests).

## Run

From this folder. The argument is the file name without `.xml`. Files are written to `logs/`.

```bash
mvn test
mvn -q compile exec:java -Dexec.args=levels
APP_ENV=prod mvn -q compile exec:java -Dexec.args=conditional
```

The `<condition>` element in `conditional.xml` needs Logback 1.5.20 or later.
