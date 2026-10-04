package com.howtodoinjava.demo.log4j2.console;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Writes one message per log level to the console through a Log4j2 Console appender.
 *
 * <p>The first program argument picks the configuration file from src/main/resources, for example
 * "log4j2-console/log4j2-color.xml". Without an argument, the plain "log4j2-console/log4j2.xml" is used.
 * All example files sit in one folder, so this example points Log4j2 to the chosen file with the
 * "log4j2.configurationFile" system property before the first logger is created.
 */
public class ConsoleAppenderExample {

  public static void main(String[] args) {
    String config = args.length > 0 ? args[0] : "log4j2-console/log4j2.xml";
    System.setProperty("log4j2.configurationFile", config);

    logRecipes(LogManager.getLogger(ConsoleAppenderExample.class));
  }

  static void logRecipes(Logger logger) {
    logger.debug("Loading recipe '{}'", "pancakes");
    logger.info("Recipe '{}' saved with {} steps", "pancakes", 4);
    logger.warn("Recipe '{}' has no steps", "waffles");
    logger.error("Could not load recipe '{}'", "soup",
        new IllegalStateException("File not found: soup.txt"));
  }
}
