package com.howtodoinjava.levels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.howtodoinjava.levels.recipes.RecipeService;

/**
 * Logs one message at every level from two loggers.
 * The root logger is set to WARN in log4j2.xml, so this class prints WARN, ERROR and FATAL.
 * The com.howtodoinjava.levels.recipes package is set to DEBUG, so RecipeService prints DEBUG and above.
 */
public class LogLevelsDemo {

  private static final Logger logger = LogManager.getLogger(LogLevelsDemo.class);

  public static void main(String[] args) {
    System.out.println("--- LogLevelsDemo (root logger, level WARN) ---");
    logAtEveryLevel(logger);

    System.out.println("--- RecipeService (package logger, level DEBUG) ---");
    RecipeService service = new RecipeService();
    service.findRecipe("pancakes");
  }

  static void logAtEveryLevel(Logger logger) {
    logger.trace("Trace message");
    logger.debug("Debug message");
    logger.info("Info message");
    logger.warn("Warn message");
    logger.error("Error message");
    logger.fatal("Fatal message");
  }
}
