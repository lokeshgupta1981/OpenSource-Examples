package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class SerializeDeserializeSet {

  public static void main(String[] args) {
    Set<Item> itemSet = Set.of(new Item(1, "item1"), new Item(2, "item2"), new Item(3, "item3"));
    String json = new Gson().toJson(itemSet);
    System.out.println(json);

    Type setType = new TypeToken<HashSet<Item>>(){}.getType();
    Set<Item> items = new Gson().fromJson(json, setType);
    System.out.println(items);
  }
}
