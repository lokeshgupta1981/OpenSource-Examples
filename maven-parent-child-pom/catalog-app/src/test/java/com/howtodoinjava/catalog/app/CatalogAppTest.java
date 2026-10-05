package com.howtodoinjava.catalog.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CatalogAppTest {

  @Test
  void printsSummary() {
    assertEquals("3 books, 2 by Bloch", CatalogApp.summary(CatalogApp.sampleCatalog()));
  }
}
