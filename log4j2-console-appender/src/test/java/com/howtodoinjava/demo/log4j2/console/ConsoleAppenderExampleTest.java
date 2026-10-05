package com.howtodoinjava.demo.log4j2.console;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Runs the example with each configuration file and checks what reaches System.out and System.err.
 * The Console appender takes System.out/System.err when the configuration starts, so the test
 * replaces both streams before it loads the configuration.
 */
class ConsoleAppenderExampleTest {

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private final ByteArrayOutputStream err = new ByteArrayOutputStream();
  private PrintStream originalOut;
  private PrintStream originalErr;
  private LoggerContext context;

  @BeforeEach
  void captureStreams() {
    originalOut = System.out;
    originalErr = System.err;
    System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    System.setErr(new PrintStream(err, true, StandardCharsets.UTF_8));
  }

  @AfterEach
  void restoreStreams() {
    Configurator.shutdown(context);
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  private void run(String config) {
    context = Configurator.initialize("test-" + config, config);
    ConsoleAppenderExample.logRecipes(context.getLogger(ConsoleAppenderExample.class));
  }

  private String stdout() {
    return out.toString(StandardCharsets.UTF_8);
  }

  private String stderr() {
    return err.toString(StandardCharsets.UTF_8);
  }

  @Test
  void xmlConfiguration() {
    run("log4j2-console/log4j2.xml");
    assertPlainOutput();
  }

  @Test
  void propertiesConfiguration() {
    run("log4j2-console/log4j2.properties");
    assertPlainOutput();
  }

  @Test
  void yamlConfiguration() {
    run("log4j2-console/log4j2.yaml");
    assertPlainOutput();
  }

  private void assertPlainOutput() {
    assertTrue(stdout().contains("INFO  [main] ConsoleAppenderExample - Recipe 'pancakes' saved with 4 steps"));
    assertTrue(stdout().contains("WARN  [main] ConsoleAppenderExample - Recipe 'waffles' has no steps"));
    assertTrue(stdout().contains("ERROR [main] ConsoleAppenderExample - Could not load recipe 'soup'"));
    assertTrue(stdout().contains("java.lang.IllegalStateException: File not found: soup.txt"));
    assertFalse(stdout().contains("DEBUG"));   // root level is info
    assertTrue(stderr().isEmpty());            // target is SYSTEM_OUT
  }

  @Test
  void thresholdFiltersSplitStdoutAndStderr() {
    run("log4j2-console/log4j2-split.xml");

    assertTrue(stdout().contains("DEBUG ConsoleAppenderExample - Loading recipe 'pancakes'"));
    assertTrue(stdout().contains("INFO  ConsoleAppenderExample - Recipe 'pancakes' saved with 4 steps"));
    assertFalse(stdout().contains("WARN"));
    assertTrue(stderr().contains("WARN  ConsoleAppenderExample - Recipe 'waffles' has no steps"));
    assertTrue(stderr().contains("ERROR ConsoleAppenderExample - Could not load recipe 'soup'"));
    assertFalse(stderr().contains("INFO"));
  }

  @Test
  void highlightAddsAnsiColorCodes() {
    run("log4j2-console/log4j2-color.xml");

    assertTrue(stdout().contains("\u001B[32mINFO \u001B[m"));    // green
    assertTrue(stdout().contains("\u001B[33mWARN \u001B[m"));    // yellow
    assertTrue(stdout().contains("\u001B[1;31mERROR\u001B[m"));  // bold red
  }

  @Test
  void jsonTemplateLayoutPrintsOneJsonObjectPerLine() {
    run("log4j2-console/log4j2-json.xml");

    String[] lines = stdout().strip().split("\\R");
    assertEquals(3, lines.length);
    assertTrue(lines[0].startsWith("{\"@timestamp\":"));
    assertTrue(lines[1].contains("\"log.level\":\"WARN\",\"message\":\"Recipe 'waffles' has no steps\""));
    assertTrue(lines[2].contains("\"error.type\":\"java.lang.IllegalStateException\""));
  }
}
