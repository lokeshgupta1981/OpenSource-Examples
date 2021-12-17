package com.howtodoinjava.demo.lombok;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;
import lombok.ToString;

@Builder(toBuilder = true)
@Getter
@ToString
@JsonDeserialize(builder = Article.ArticleBuilder.class)
public class Article {
  @NonNull
  private final Long id;
  private String title = "Title Placeholder";
  @Singular
  private final List<String> tags;

  public static ArticleBuilder builder(final Long id) {
    return new ArticleBuilder().id(id);
  } 
  
  @JsonPOJOBuilder(withPrefix="")
  public static class ArticleBuilder {
  }
}
