package com.howtodoinjava.demo.lombok;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class DeserializationDemo {
  public static void main(final String[] args)
    throws JsonMappingException, JsonProcessingException {

    String json = "{\"id\":1,\"title\":\"Test Title\",\"tags\":[\"Test Tag\"]}";

    ObjectMapper mapper = new ObjectMapper();
    Article article = mapper.readValue(json, Article.class);
    
    System.out.println(article);

    Gson gson = new Gson();
    Article gsonArticle = gson.fromJson(json, Article.class);
    
    System.out.println(gsonArticle);
  }
}
