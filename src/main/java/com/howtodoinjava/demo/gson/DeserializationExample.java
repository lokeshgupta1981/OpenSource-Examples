package com.howtodoinjava.demo.gson;

import java.time.LocalDate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.howtodoinjava.demo.model.User;

public class DeserializationExample {
  public static void main(final String[] args) {
    
    String jsonString = """
      {  
        "id": 1,
        "firstName": "lokesh",
        "lastName": "gupta",
        "dateOfbirth": "1999-01-01",
        "department": {
          "id": 2,
          "name": "IT",
          "active": true
        }
      }""";
    
    Gson gson = new GsonBuilder()
      .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
      .create();

    User user = gson.fromJson(jsonString, User.class);

    System.out.println(user);
  }
}
