package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;

/**
 * Gson 2.10 and newer read and write Java records through the canonical constructor.
 */
public class RecordExample {

  record Author(String name, String country) {
  }

  public static void main(String[] args) {
    Gson gson = new Gson();
    String json = gson.toJson(new Author("Joshua Bloch", "USA"));
    System.out.println(json);

    Author author = gson.fromJson("{\"name\":\"Robert Martin\",\"country\":\"USA\"}", Author.class);
    System.out.println(author);
  }
}
