package com.howtodoinjava.catalog;

import java.util.Objects;

public record Book(String title, String author, int year) {

  public Book {
    Objects.requireNonNull(title, "title");
    Objects.requireNonNull(author, "author");
    if (title.isBlank()) {
      throw new IllegalArgumentException("title must not be blank");
    }
    if (year <= 0) {
      throw new IllegalArgumentException("year must be positive: " + year);
    }
  }
}
