package com.howtodoinjava.mcp;

import java.util.Map;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

@Component
public class LibraryTools {

  private final Map<String, Integer> copies = Map.of(
      "clean code", 2, "effective java", 0, "refactoring", 1);

  @McpTool(name = "check_availability",
      description = "Returns how many copies of a book the library has on the shelf")
  public String checkAvailability(
      @McpToolParam(description = "Book title", required = true) String title) {

    Integer count = copies.get(title.toLowerCase());
    if (count == null) {
      throw new IllegalArgumentException("No book with the title '" + title + "' in the catalog");
    }
    return count + " copies of '" + title + "' available";
  }
}
