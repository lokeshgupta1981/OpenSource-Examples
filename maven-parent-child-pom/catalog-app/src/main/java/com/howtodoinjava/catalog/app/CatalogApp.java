package com.howtodoinjava.catalog.app;

import com.howtodoinjava.catalog.Book;
import com.howtodoinjava.catalog.Catalog;

public class CatalogApp {

  public static Catalog sampleCatalog() {
    return new Catalog()
        .add(new Book("Effective Java", "Bloch", 2018))
        .add(new Book("Java Puzzlers", "Bloch", 2005))
        .add(new Book("Clean Code", "Martin", 2008));
  }

  public static String summary(Catalog catalog) {
    return "%d books, %d by Bloch".formatted(catalog.size(), catalog.byAuthor("Bloch").size());
  }

  public static void main(String[] args) {
    System.out.println(summary(sampleCatalog()));
  }
}
