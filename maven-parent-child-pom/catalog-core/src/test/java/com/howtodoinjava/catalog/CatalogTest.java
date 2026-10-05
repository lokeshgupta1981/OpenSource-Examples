package com.howtodoinjava.catalog;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CatalogTest {

  @Test
  void findsBooksByAuthor() {
    Catalog catalog = new Catalog()
        .add(new Book("Effective Java", "Bloch", 2018))
        .add(new Book("Java Puzzlers", "Bloch", 2005))
        .add(new Book("Clean Code", "Martin", 2008));

    assertEquals(3, catalog.size());
    assertEquals(2, catalog.byAuthor("Bloch").size());
    assertEquals(0, catalog.byAuthor("Unknown").size());
  }

  @Test
  void rejectsInvalidBooks() {
    assertThrows(IllegalArgumentException.class, () -> new Book(" ", "Bloch", 2018));
    assertThrows(IllegalArgumentException.class, () -> new Book("Effective Java", "Bloch", 0));
    assertThrows(NullPointerException.class, () -> new Catalog().add(null));
  }
}
