package com.howtodoinjava.demo.lombok;

public class BuilderExample {
  public static void main(final String[] args) {
    Article article = Article.builder(1L)
        .title("Test Article")
        .tag("Data")
        .build();

    Article newArticle = article.toBuilder()
        .title("Modified Title")
        .build();
  }
}