Source code for the article https://howtodoinjava.com/log4j2/logging-levels/

# Log4j 2 Logging Levels

Prints one message at every level (TRACE to FATAL) from two loggers. The root logger is set to WARN in
*log4j2.xml*, and the *com.howtodoinjava.levels.recipes* package is set to DEBUG, so the console shows which
messages each level lets through. The tests check the enabled levels, the order of the standard levels and a
level change at runtime with *Configurator.setLevel()*.

## Versions

- Java 25
- Log4j 2.26.1 (log4j-api, log4j-core)
- JUnit 6.1.3 (JUnit Jupiter)
- Maven 3.9+

## Run

Run the tests:

```bash
mvn test
```

Run the demo with *log4j2.xml*:

```bash
mvn -q exec:java
```

Run the same demo with the properties format of the configuration:

```bash
mvn -q exec:java -Dlog4j2.configurationFile=src/main/resources/properties-format/log4j2.properties
```

Output of the demo:

```
--- LogLevelsDemo (root logger, level WARN) ---
WARN  LogLevelsDemo - Warn message
ERROR LogLevelsDemo - Error message
FATAL LogLevelsDemo - Fatal message
--- RecipeService (package logger, level DEBUG) ---
DEBUG RecipeService - Looking up recipe pancakes in the cache
INFO  RecipeService - Recipe pancakes loaded
WARN  RecipeService - Recipe pancakes has no cooking time
ERROR RecipeService - Could not load the image of recipe pancakes
FATAL RecipeService - Recipe database is not reachable
```

## Files

| File | What it shows |
|---|---|
| src/main/resources/log4j2.xml | Root logger at WARN, package logger at DEBUG |
| src/main/resources/properties-format/log4j2.properties | The same levels in the properties format |
| LogLevelsDemo | Logs at every level with a logger that inherits the root level |
| recipes/RecipeService | Logs at every level with a logger in the DEBUG package |
| LogLevelsTest | Enabled levels per logger, level order and integer values, runtime level change |
