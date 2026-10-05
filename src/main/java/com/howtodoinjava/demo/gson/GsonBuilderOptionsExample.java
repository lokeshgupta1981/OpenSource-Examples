package com.howtodoinjava.demo.gson;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.List;

/**
 * The GsonBuilder options readers ask for most: pretty printing, nulls,
 * date format and field naming.
 */
public class GsonBuilderOptionsExample {

  public static void main(String[] args) {
    Book book = new Book(1, "Effective Java", "Joshua Bloch", 412, List.of("java", "design"));
    Book noAuthor = new Book(2, "Clean Code", null, 464, null);

    // 1. Pretty printing
    Gson pretty = new GsonBuilder().setPrettyPrinting().create();
    System.out.println(pretty.toJson(book));

    // 2. Null fields: dropped by default, kept with serializeNulls()
    String withoutNulls = new Gson().toJson(noAuthor);
    System.out.println(withoutNulls);
    String withNulls = new GsonBuilder().serializeNulls().create().toJson(noAuthor);
    System.out.println(withNulls);

    // 3. Date format for java.util.Date
    Date added = new Date(1735689600000L); // 2025-01-01T00:00:00Z
    LibraryEntry entry = new LibraryEntry(book.getTitle(), added);
    String defaultDate = new Gson().toJson(entry);
    System.out.println(defaultDate);
    Gson dated = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
    System.out.println(dated.toJson(entry));

    // 4. Field naming policy
    Gson snake = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    System.out.println(snake.toJson(entry));

    // 5. All together
    Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .serializeNulls()
        .setDateFormat("yyyy-MM-dd")
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    System.out.println(gson.toJson(entry));
  }

  /** A small class with a camelCase field and a java.util.Date field. */
  static class LibraryEntry {
    private String bookTitle;
    private Date addedOn;

    LibraryEntry(String bookTitle, Date addedOn) {
      this.bookTitle = bookTitle;
      this.addedOn = addedOn;
    }
  }
}
