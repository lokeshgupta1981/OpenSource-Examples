package com.howtodoinjava.demo.lombok;

public class BuilderExample 
{
  public static void main(final String[] args) {
    Article a = Article.builder(1L)
      .title("Test Article")
      .tag("Data")
      .build();
    
    System.out.println(a);
    
    Article b = a.toBuilder()
      .title("Final Article") 
      .build();
    
    System.out.println(b);
  }
}