package com.howtodoinjava.demo.lombok;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class SerializationDemo {
  public static void main(final String[] args) throws JsonProcessingException {
    Article article = Article.builder(1L)
      .title("Test Article")
      .tag("Test Tag")
      .build();
    
    //With Jackson
    ObjectMapper mapper = new ObjectMapper();
    String json = mapper.writeValueAsString(article);
    System.out.println(json);
    
    //With Gson
    Gson gson = new Gson();
    String gsonJson = gson.toJson(article);
    System.out.println(gsonJson);
  }
}
