package com.howtodoinjava.demo.logback.library;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Searches the book catalog. Used to show per-class log levels.
 */
public class CatalogService {

  private static final Logger log = LoggerFactory.getLogger(CatalogService.class);

  private final List<String> books = List.of("Dune", "Emma", "Ulysses");

  public List<String> search(String text) {
    log.debug("Searching for '{}'", text);
    List<String> found = books.stream()
        .filter(b -> b.toLowerCase().contains(text.toLowerCase()))
        .toList();
    log.info("Found {} books for '{}'", found.size(), text);
    return found;
  }
}
