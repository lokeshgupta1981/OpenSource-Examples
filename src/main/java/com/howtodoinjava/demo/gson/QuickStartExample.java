package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.util.List;

/**
 * Object to JSON with toJson() and JSON to object with fromJson().
 */
public class QuickStartExample {

  public static void main(String[] args) {
    Gson gson = new Gson();

    // Java object -> JSON string
    Book book = new Book(1, "Effective Java", "Joshua Bloch", 412, List.of("java", "design"));
    String json = gson.toJson(book);
    System.out.println(json);

    // JSON string -> Java object
    Book parsed = gson.fromJson(json, Book.class);
    System.out.println(parsed);
    System.out.println("parsed.getTitle() = " + parsed.getTitle());
    System.out.println("book.equals(parsed) = " + book.equals(parsed));

    // A List needs a TypeToken when we read it back
    String listJson = gson.toJson(List.of(book));
    System.out.println(listJson);
    List<Book> books = gson.fromJson(listJson, new TypeToken<List<Book>>() {});
    System.out.println("books.get(0).getTitle() = " + books.get(0).getTitle());

    // GsonBuilder changes how the JSON looks
    Gson pretty = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
    System.out.println(pretty.toJson(book));

    // Fields missing in the JSON keep their default value
    Book partial = gson.fromJson("{\"id\":2,\"title\":\"Clean Code\"}", Book.class);
    System.out.println(partial);

    // Unknown JSON fields are ignored
    Book extra = gson.fromJson("{\"id\":3,\"title\":\"Refactoring\",\"isbn\":\"0134757599\"}", Book.class);
    System.out.println(extra);

    // Broken JSON throws JsonSyntaxException
    try {
      Book bad = gson.fromJson("{\"id\":4,\"title\":", Book.class);
      System.out.println(bad);
    } catch (JsonSyntaxException e) {
      System.out.println("JsonSyntaxException: " + e.getMessage());
    }
  }
}
