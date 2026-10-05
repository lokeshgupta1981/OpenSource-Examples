package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * The tree model: JsonParser turns a JSON string into a JsonElement
 * tree that we can read and change without a Java class.
 */
public class JsonTreeExample {

  public static void main(String[] args) {
    String json = """
        {
          "id": 1,
          "title": "Effective Java",
          "author": "Joshua Bloch",
          "publisher": {"name": "Addison-Wesley", "country": "USA"},
          "tags": ["java", "design"],
          "ebook": null
        }
        """;

    // 1. Parse into a tree
    JsonElement root = JsonParser.parseString(json);
    JsonObject book = root.getAsJsonObject();

    // 2. Read values
    int id = book.get("id").getAsInt();
    String title = book.get("title").getAsString();
    String publisher = book.getAsJsonObject("publisher").get("name").getAsString();
    JsonArray tags = book.getAsJsonArray("tags");
    String firstTag = tags.get(0).getAsString();
    int tagCount = tags.size();
    boolean hasIsbn = book.has("isbn");
    boolean ebookIsNull = book.get("ebook").isJsonNull();
    System.out.println(id + " | " + title + " | " + publisher + " | " + firstTag
        + " | " + tagCount + " | " + hasIsbn + " | " + ebookIsNull);

    // 3. A missing key returns null, so check it before calling getAs...
    JsonElement missing = book.get("isbn");
    System.out.println("missing = " + missing);

    // 4. Change the tree and write it back
    book.addProperty("pages", 412);
    book.remove("ebook");
    tags.add("bestseller");
    System.out.println(new Gson().toJson(book));

    // 5. Build a tree from scratch
    JsonObject note = new JsonObject();
    note.addProperty("bookId", 1);
    note.addProperty("text", "Great chapter on generics");
    System.out.println(note);

    // 6. Tree <-> object
    Book asBook = new Gson().fromJson(book, Book.class);
    System.out.println(asBook.getTitle() + " / " + asBook.getPages());
    JsonElement tree = new Gson().toJsonTree(asBook);
    System.out.println(tree.getAsJsonObject().get("pages"));
  }
}
