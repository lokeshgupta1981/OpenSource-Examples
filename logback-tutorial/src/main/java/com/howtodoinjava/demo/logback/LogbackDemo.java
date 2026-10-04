package com.howtodoinjava.demo.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.howtodoinjava.demo.logback.library.CatalogService;
import com.howtodoinjava.demo.logback.library.LoanService;

import ch.qos.logback.classic.LoggerContext;

/**
 * Runs the library example with one of the configuration files in
 * src/main/resources/logback-examples.
 *
 * <pre>
 * mvn -q compile exec:java -Dexec.args=console
 * </pre>
 *
 * The argument is the file name without ".xml" (console, levels, additivity-duplicate,
 * additivity-fixed, rolling, mdc, async, conditional, broken). With the argument "default",
 * no file is set and Logback uses logback-test.xml, logback.xml or its built-in default.
 */
public class LogbackDemo {

  public static void main(String[] args) {
    String config = args.length > 0 ? args[0] : "console";
    if (!"default".equals(config)) {
      // must be set before the first LoggerFactory call
      System.setProperty("logback.configurationFile", "logback-examples/" + config + ".xml");
    }

    Logger log = LoggerFactory.getLogger(LogbackDemo.class);
    LoanService loans = new LoanService();
    CatalogService catalog = new CatalogService();

    log.info("Library opened");
    MDC.put("member", "Lokesh");
    try {
      catalog.search("dune");
      loans.borrow("Lokesh", "Dune");
      loans.borrow("Lokesh", "Dune");
      loans.giveBack("Emma");
      if ("rolling".equals(config)) {
        for (int i = 1; i <= 40; i++) {
          loans.borrow("Lokesh", "Book " + i);
        }
      }
    } finally {
      MDC.remove("member");
    }

    // flushes and closes all appenders (needed for AsyncAppender)
    ((LoggerContext) LoggerFactory.getILoggerFactory()).stop();
  }
}
