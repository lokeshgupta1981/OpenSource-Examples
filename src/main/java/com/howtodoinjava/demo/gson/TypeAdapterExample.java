package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDate;

/**
 * Registers a custom TypeAdapter for LocalDate and compares it with
 * the default output.
 */
public class TypeAdapterExample {

  public static void main(String[] args) {
    Loan loan = new Loan(1, "Effective Java", LocalDate.of(2026, 10, 6));

    // Default: Gson writes the fields of LocalDate as a JSON object
    String defaultJson = new Gson().toJson(loan);
    System.out.println(defaultJson);

    // Custom format with our own TypeAdapter
    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
        .create();
    String json = gson.toJson(loan);
    System.out.println(json);

    Loan parsed = gson.fromJson(json, Loan.class);
    LocalDate dueOn = parsed.getDueOn();
    System.out.println(dueOn + " | " + dueOn.getClass().getSimpleName());

    // The adapter handles JSON null as well
    Loan open = gson.fromJson("{\"bookId\":2,\"title\":\"Clean Code\",\"dueOn\":null}", Loan.class);
    System.out.println(open.getDueOn());
  }

  static class Loan {
    private int bookId;
    private String title;
    private LocalDate dueOn;

    Loan(int bookId, String title, LocalDate dueOn) {
      this.bookId = bookId;
      this.title = title;
      this.dueOn = dueOn;
    }

    LocalDate getDueOn() {
      return dueOn;
    }
  }
}
