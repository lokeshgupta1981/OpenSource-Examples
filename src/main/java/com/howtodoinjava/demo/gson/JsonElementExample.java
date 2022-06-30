package com.howtodoinjava.demo.gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JsonElementExample {
  public static void main(String[] args) {
    String json = "{'id': 1001, "
        + "'firstName': 'Lokesh',"
        + "'lastName': 'Gupta',"
        + "'email': 'howtodoinjava@gmail.com'}";

    JsonElement jsonElement = JsonParser.parseString(json);

    JsonObject jsonObject = jsonElement.getAsJsonObject();

    System.out.println( jsonObject.get("id") );
    System.out.println( jsonObject.get("firstName") );
    System.out.println( jsonObject.get("lastName") );
    System.out.println( jsonObject.get("email") );
  }
}

