package com.howtodoinjava.demo.logback.library;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lends books to library members and logs each step at a different level.
 */
public class LoanService {

  private static final Logger log = LoggerFactory.getLogger(LoanService.class);

  private final Map<String, String> loans = new HashMap<>();   // book -> member

  public boolean borrow(String member, String book) {
    log.debug("Checking book '{}'", book);
    if (loans.containsKey(book)) {
      log.warn("Book '{}' is already borrowed", book);
      return false;
    }
    loans.put(book, member);
    log.info("Book '{}' borrowed by {}", book, member);
    return true;
  }

  public boolean giveBack(String book) {
    if (loans.remove(book) == null) {
      log.error("Cannot return '{}': no loan found", book);
      return false;
    }
    log.info("Book '{}' returned", book);
    return true;
  }
}
