package com.howtodoinjava.demo.logback;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.howtodoinjava.demo.logback.library.CatalogService;
import com.howtodoinjava.demo.logback.library.LoanService;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.status.Status;

/**
 * Loads each file in logback-examples and checks the log lines shown in the article.
 */
class LogbackConfigTest {

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private PrintStream originalOut;
  private LoggerContext context;

  @BeforeEach
  void captureConsole() {
    originalOut = System.out;
    System.setOut(new PrintStream(out, true, StandardCharsets.UTF_8));
    context = (LoggerContext) LoggerFactory.getILoggerFactory();
  }

  @AfterEach
  void restore() throws IOException {
    System.setOut(originalOut);
    System.clearProperty("APP_ENV");
    context.reset();
    deleteLogs();
  }

  @Test
  void consoleConfigPrintsInfoAndAbove() throws Exception {
    configure("console");
    runLibrary();
    String console = console();
    assertTrue(console.contains("[main] INFO  c.h.demo.logback.library.LoanService - Book 'Dune' borrowed by Lokesh"));
    assertTrue(console.contains("ERROR c.h.demo.logback.library.LoanService - Cannot return 'Emma': no loan found"));
    assertFalse(console.contains("Checking book"));          // DEBUG is below INFO
  }

  @Test
  void loggerElementsSetLevelsPerPackageAndClass() throws Exception {
    configure("levels");
    runLibrary();
    String console = console();
    assertTrue(console.contains("DEBUG c.h.demo.logback.library.LoanService - Checking book 'Dune'"));
    assertFalse(console.contains("CatalogService"));         // WARN only for CatalogService
  }

  @Test
  void appenderOnLoggerAndRootPrintsEachLineTwice() throws Exception {
    configure("additivity-duplicate");
    runLibrary();
    assertEquals(2, count(console(), "Book 'Dune' is already borrowed"));
    assertEquals(1, count(console(), "Library opened"));       // LogbackDemo is outside the package
  }

  @Test
  void additivityFalseKeepsLibraryLinesOutOfTheConsole() throws Exception {
    configure("additivity-fixed");
    runLibrary();
    context.reset();
    assertEquals("INFO  c.h.demo.logback.LogbackDemo - Library opened", console().strip());
    String file = Files.readString(Path.of("logs/loans.log"));
    assertTrue(file.contains("DEBUG c.h.demo.logback.library.LoanService - Checking book 'Dune'"));
  }

  @Test
  void mdcValueAppearsInEveryLine() throws Exception {
    configure("mdc");
    runLibrary();
    String console = console();
    assertTrue(console.contains("INFO  [guest] c.h.d.l.LogbackDemo - Library opened"));
    assertTrue(console.contains("WARN  [Lokesh] c.h.d.l.l.LoanService - Book 'Dune' is already borrowed"));
  }

  @Test
  void rollingPolicyRollsOverWhenTheFileReachesMaxFileSize() throws Exception {
    configure("rolling");
    LoanService loans = new LoanService();
    for (int i = 1; i <= 40; i++) {
      loans.borrow("Lokesh", "Book " + i);
    }
    String status = statusMessages();
    context.reset();
    // the archive names are checked through the status messages, not the folder:
    // with a 1KB limit, Logback's start-up cleanup can race with the very first rollovers
    assertTrue(status.contains("Renaming file [logs/library.log] to [logs/library-"), status);
    assertTrue(status.contains(".0.log.gz]"), status);
    assertTrue(Files.exists(Path.of("logs/library.log")));
  }

  @Test
  void asyncAppenderWritesAllLinesAfterStop() throws Exception {
    configure("async");
    runLibrary();
    context.reset();                                       // stops the appenders, drains the queue
    assertEquals(5, Files.readAllLines(Path.of("logs/async.log")).size());
  }

  @Test
  void conditionPicksTheRootLevel() throws Exception {
    configure("conditional");
    runLibrary();
    assertTrue(console().contains("DEBUG c.h.demo.logback.library.LoanService - Checking book 'Dune'"));

    out.reset();
    context.reset();
    System.setProperty("APP_ENV", "prod");
    configure("conditional");
    runLibrary();
    assertEquals(2, console().strip().lines().count());   // WARN and ERROR only
  }

  @Test
  void typoInAppenderRefIsReportedAsStatusWarning() throws Exception {
    configure("broken");
    assertTrue(statusMessages().contains("Appender named [CONSOL] could not be found. Skipping attachment to Logger[ROOT]."));
  }

  @Test
  void janinoStyleIfConditionIsIgnored() throws Exception {
    configure("legacy-if");
    runLibrary();
    assertEquals("", console());                            // no root appender at all
    assertTrue(statusMessages().contains("The 'condition' attribute in <if> element is deprecated"));
  }

  private void configure(String name) throws Exception {
    context.reset();
    context.getStatusManager().clear();
    JoranConfigurator configurator = new JoranConfigurator();
    configurator.setContext(context);
    configurator.doConfigure(getClass().getResource("/logback-examples/" + name + ".xml"));
  }

  private static void runLibrary() {
    Logger log = LoggerFactory.getLogger(LogbackDemo.class);
    LoanService loans = new LoanService();
    log.info("Library opened");
    MDC.put("member", "Lokesh");
    try {
      new CatalogService().search("dune");
      loans.borrow("Lokesh", "Dune");
      loans.borrow("Lokesh", "Dune");
      loans.giveBack("Emma");
    } finally {
      MDC.remove("member");
    }
  }

  private String console() {
    return out.toString(StandardCharsets.UTF_8);
  }

  private String statusMessages() {
    StringBuilder sb = new StringBuilder();
    for (Status s : context.getStatusManager().getCopyOfStatusList()) {
      sb.append(s.getMessage()).append('\n');
    }
    return sb.toString();
  }

  private static int count(String text, String part) {
    return text.split(java.util.regex.Pattern.quote(part), -1).length - 1;
  }

  private static void deleteLogs() throws IOException {
    Path logs = Path.of("logs");
    if (Files.exists(logs)) {
      try (Stream<Path> walk = Files.walk(logs)) {
        walk.sorted(Comparator.reverseOrder()).forEach(p -> p.toFile().delete());
      }
    }
  }
}
