# Log4j2 Console Appender Examples

Source code for the article [Log4j2 ConsoleAppender Example](https://howtodoinjava.com/log4j2/log4j2-consoleappender-example/).

A self-contained Maven module, separate from the root pom.xml of this repository.
Tested with Java 25, Log4j 2.26.1, Jackson 2.22.3 and JUnit 6.1.3.

## Files

| File | What it shows |
|---|---|
| `ConsoleAppenderExample.java` | One message per level, plus an exception |
| `Slf4jConsoleExample.java` | The same Console appender used through the SLF4J API |
| `src/main/resources/log4j2-console/log4j2.xml` | Plain Console appender with a PatternLayout |
| `src/main/resources/log4j2-console/log4j2.properties` | The same configuration in properties format |
| `src/main/resources/log4j2-console/log4j2.yaml` | The same configuration in YAML format |
| `src/main/resources/log4j2-console/log4j2-split.xml` | DEBUG and INFO to System.out, WARN and above to System.err (ThresholdFilter) |
| `src/main/resources/log4j2-console/log4j2-color.xml` | Colored levels with %highlight and %style |
| `src/main/resources/log4j2-console/log4j2-json.xml` | One JSON object per line with JsonTemplateLayout |
| `ConsoleAppenderExampleTest.java` | Checks what reaches System.out and System.err for each file |

The configuration files live in the `log4j2-console` folder. `ConsoleAppenderExample` selects one with the
`log4j2.configurationFile` system property (first program argument).

## Dependencies

All versions are in `pom.xml`: `log4j-api` and `log4j-core`, plus `log4j-layout-template-json` (JSON layout),
`jackson-dataformat-yaml` (YAML config) and `log4j-slf4j2-impl` (SLF4J 2.x bridge).

## Run

```bash
cd log4j2-console-appender
mvn -q compile exec:java -Dexec.args="log4j2-console/log4j2-color.xml"
mvn test
```
