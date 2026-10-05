package com.howtodoinjava.jackson.javatime;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * The Jackson 2.x mappers used in the article, built in the three common ways.
 */
public final class JavaTimeMappers {

  private JavaTimeMappers() {
  }

  /** A mapper without the module. Serializing java.time values with it fails. */
  public static ObjectMapper bareMapper() {
    return new ObjectMapper();
  }

  /** Fix 1: register the module on an existing ObjectMapper. */
  public static ObjectMapper withRegisterModule() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  /** Fix 2: let Jackson find every module on the classpath (ServiceLoader). */
  public static ObjectMapper withFindAndRegisterModules() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.findAndRegisterModules();
    return mapper;
  }

  /** Fix 3: the builder style, with ISO-8601 strings instead of timestamps. */
  public static JsonMapper isoMapper() {
    return JsonMapper.builder()
        .addModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .build();
  }
}
