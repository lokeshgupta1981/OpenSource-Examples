package com.howtodoinjava.levels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.junit.jupiter.api.Test;

import com.howtodoinjava.levels.recipes.RecipeService;

/**
 * Checks which levels are enabled for the root logger (WARN) and the recipes package logger (DEBUG)
 * configured in log4j2.xml, the order of the standard levels, and a level change at runtime.
 */
class LogLevelsTest {

  private static final Logger rootLevelLogger = LogManager.getLogger(LogLevelsDemo.class);
  private static final Logger packageLogger = LogManager.getLogger(RecipeService.class);

  @Test
  void rootLoggerAtWarnPrintsWarnErrorAndFatalOnly() {
    assertEquals(Level.WARN, rootLevelLogger.getLevel());

    assertFalse(rootLevelLogger.isTraceEnabled());
    assertFalse(rootLevelLogger.isDebugEnabled());
    assertFalse(rootLevelLogger.isInfoEnabled());
    assertTrue(rootLevelLogger.isWarnEnabled());
    assertTrue(rootLevelLogger.isErrorEnabled());
    assertTrue(rootLevelLogger.isFatalEnabled());

    LogLevelsDemo.logAtEveryLevel(rootLevelLogger);
  }

  @Test
  void packageLoggerAtDebugPrintsDebugAndAbove() {
    assertEquals(Level.DEBUG, packageLogger.getLevel());

    assertFalse(packageLogger.isTraceEnabled());
    assertTrue(packageLogger.isDebugEnabled());
    assertTrue(packageLogger.isInfoEnabled());
    assertTrue(packageLogger.isWarnEnabled());

    new RecipeService().findRecipe("pancakes");
  }

  @Test
  void standardLevelsAreOrderedBySeverity() {
    assertEquals(0, Level.OFF.intLevel());
    assertEquals(100, Level.FATAL.intLevel());
    assertEquals(200, Level.ERROR.intLevel());
    assertEquals(300, Level.WARN.intLevel());
    assertEquals(400, Level.INFO.intLevel());
    assertEquals(500, Level.DEBUG.intLevel());
    assertEquals(600, Level.TRACE.intLevel());
    assertEquals(Integer.MAX_VALUE, Level.ALL.intLevel());

    // A logger set to WARN accepts a message when the message level is "more specific" (lower int) or equal
    assertTrue(Level.ERROR.isMoreSpecificThan(Level.WARN));
    assertTrue(Level.WARN.isMoreSpecificThan(Level.WARN));
    assertFalse(Level.INFO.isMoreSpecificThan(Level.WARN));
  }

  @Test
  void levelCanBeChangedAtRuntime() {
    Logger logger = LogManager.getLogger("com.howtodoinjava.levels.runtime.Checkout");
    assertEquals(Level.WARN, logger.getLevel());         // inherited from the root logger
    assertFalse(logger.isInfoEnabled());

    Configurator.setLevel(logger.getName(), Level.INFO);
    assertEquals(Level.INFO, logger.getLevel());
    assertTrue(logger.isInfoEnabled());
    logger.info("Info is enabled after Configurator.setLevel()");

    Configurator.setLevel(logger.getName(), Level.WARN);  // back to the configured level
    assertFalse(logger.isInfoEnabled());
  }

  @Test
  void customLevelSitsBetweenWarnAndInfo() {
    Level notice = Level.getLevel("NOTICE");
    assertEquals(350, notice.intLevel());

    assertTrue(packageLogger.isEnabled(notice));              // package logger is DEBUG
    packageLogger.log(notice, "Recipe pancakes was edited");  // printed
    assertFalse(rootLevelLogger.isEnabled(notice));           // root logger is WARN
    rootLevelLogger.log(notice, "Not printed");
  }
}
