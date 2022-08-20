package com.howtodoinjava.demo.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class XmlExamples {
  public static void main(String[] args) throws IOException {
    XmlMapper xmlMapper = new XmlMapper();
    /*xmlMapper.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);
    xmlMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    xmlMapper.disable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);
    xmlMapper.disable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);*/

    Article article = new Article(1L, "Test Title", List.of("Tag1", "Tag2"));

    //Serialize To String
    String articleXml = xmlMapper.writeValueAsString(article);
    System.out.println(articleXml);

    //Serialize To File
    xmlMapper.writeValue(new File("article-1.xml"), article);

    //Deserialize from String
    article = xmlMapper.readValue("<article id=\"1\"><title>Test " +
        "Title</title><tags><tag>Tag1</tag><tag>Tag2</tag></tags></article>", Article.class);
    System.out.println(articleXml);

    //Serialize from File
    article = xmlMapper.readValue(new File("article-1.xml"), Article.class);

    System.out.println(articleXml);
  }
}