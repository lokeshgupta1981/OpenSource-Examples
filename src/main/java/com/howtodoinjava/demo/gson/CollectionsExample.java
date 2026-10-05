package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Lists, arrays and maps need a TypeToken when we read them back,
 * because Java erases the generic type at runtime.
 */
public class CollectionsExample {

  public static void main(String[] args) {
    Gson gson = new Gson();

    Book first = new Book(1, "Effective Java", "Joshua Bloch", 412, List.of("java"));
    Book second = new Book(2, "Clean Code", "Robert Martin", 464, List.of("craft"));

    // List -> JSON array
    List<Book> books = List.of(first, second);
    String listJson = gson.toJson(books);
    System.out.println(listJson);

    // JSON array -> List<Book>
    List<Book> parsedBooks = gson.fromJson(listJson, new TypeToken<List<Book>>() {});
    System.out.println(parsedBooks.get(1).getTitle());
    System.out.println(parsedBooks.get(1).getClass().getSimpleName());

    // JSON array -> Book[]
    Book[] bookArray = gson.fromJson(listJson, Book[].class);
    System.out.println(bookArray.length);

    // Map -> JSON object
    Map<String, Integer> stock = new LinkedHashMap<>();
    stock.put("Effective Java", 5);
    stock.put("Clean Code", 3);
    String mapJson = gson.toJson(stock);
    System.out.println(mapJson);

    // JSON object -> Map<String, Integer>
    Map<String, Integer> parsedStock = gson.fromJson(mapJson, new TypeToken<Map<String, Integer>>() {});
    System.out.println(parsedStock.get("Clean Code"));

    // Without a TypeToken the values become Gson's own types
    List<?> untyped = gson.fromJson(listJson, List.class);
    System.out.println(untyped.get(0).getClass().getSimpleName());
  }
}
