package com.howtodoinjava.demo.gson;

import java.time.LocalDate;
import java.time.Month;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.howtodoinjava.demo.model.Department;
import com.howtodoinjava.demo.model.User;

public class SerializationExample {
  public static void main(final String[] args) {

    User user = new User(1L, "lokesh", "gupta",
      LocalDate.of(1999, Month.JANUARY, 1), new Department(2, "IT", true));

    Gson gson = new GsonBuilder()
      .setPrettyPrinting()
      .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
      .create();

    String jsonString = gson.toJson(user);

    System.out.println(jsonString);
  }
}
