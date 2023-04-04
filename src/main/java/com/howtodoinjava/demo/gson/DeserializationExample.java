package com.howtodoinjava.demo.gson;

import com.howtodoinjava.demo.gson.adapters.LocalDateTypeAdapter;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.howtodoinjava.demo.model.User;

public class DeserializationExample {
  public static void main(final String[] args) {
    Logger logger = LoggerFactory.getLogger(SerializationExample.class);
    
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
      .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
      .create();

    User user = gson.fromJson(jsonString, User.class);

    logger.info("Parsed Object : " + user);
  }
}
