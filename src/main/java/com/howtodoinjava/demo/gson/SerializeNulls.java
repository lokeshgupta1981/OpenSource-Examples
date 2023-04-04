package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;

public class SerializeNulls {

  public static void main(String[] args) {

    //Default behavior
    String json = new Gson().toJson(new Item(1, null));
    System.out.println(json);

    Gson gson = new GsonBuilder()
        .serializeNulls()
        .create();
    String itemJson = gson.toJson(new Item(1, null));
    System.out.println(itemJson);
  }
}
