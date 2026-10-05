package com.howtodoinjava.demo.log4j2.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs through the SLF4J API. The log4j-slf4j2-impl bridge sends the messages to Log4j2, so the same
 * Console appender and pattern print them.
 */
public class Slf4jConsoleExample {

  public static void main(String[] args) {
    String config = args.length > 0 ? args[0] : "log4j2-console/log4j2.xml";
    System.setProperty("log4j2.configurationFile", config);

    Logger logger = LoggerFactory.getLogger(Slf4jConsoleExample.class);

    logger.info("Recipe '{}' saved with {} steps", "pancakes", 4);
  }
}
