package com.howtodoinjava.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Catalog {

  private final List<Book> books = new ArrayList<>();

  public Catalog add(Book book) {
    books.add(Objects.requireNonNull(book, "book"));
    return this;
  }

  public List<Book> books() {
    return List.copyOf(books);
  }

  public List<Book> byAuthor(String author) {
    return books.stream().filter(b -> b.author().equals(author)).toList();
  }

  public int size() {
    return books.size();
  }
}
