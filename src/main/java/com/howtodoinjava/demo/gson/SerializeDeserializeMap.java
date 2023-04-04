package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.howtodoinjava.demo.gson.adapters.LocalDateTypeAdapter;
import com.howtodoinjava.demo.model.Department;
import com.howtodoinjava.demo.model.User;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class SerializeDeserializeMap {

  public static void main(String[] args) {
    Map map = Map.of(
        1, new User(1L, "lokesh", "gupta", LocalDate.of(1999, Month.JANUARY, 1),
            new Department(1, "IT", true)),
        2, new User(2L, "alex", "gussin", LocalDate.of(1988, Month.MARCH, 31),
            new Department(2, "FINANCE", true)));

    Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
        .create();

    String jsonString = gson.toJson(map);

    System.out.println(jsonString);

    Type mapType = new TypeToken<HashMap<Integer, User>>() {}.getType();
    HashMap<Long, User> usersMap = gson.fromJson(jsonString, mapType);

    System.out.println(usersMap);
  }
}
