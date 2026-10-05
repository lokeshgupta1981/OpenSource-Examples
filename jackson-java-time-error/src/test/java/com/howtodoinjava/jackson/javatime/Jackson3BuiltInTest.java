package com.howtodoinjava.jackson.javatime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

/**
 * Jackson 3 (tools.jackson) supports java.time without any module and writes ISO-8601 strings by default.
 */
class Jackson3BuiltInTest {

  private static final String ISO_JSON = "{\"title\":\"Pay rent\",\"dueOn\":\"2026-10-01\","
      + "\"dueAt\":\"2026-10-01T09:30:00\",\"createdAt\":\"2026-09-25T08:15:30Z\"}";

  @Test
  void plainJsonMapperWritesIsoStrings() {
    ObjectMapper mapper = JsonMapper.builder().build();

    String json = mapper.writeValueAsString(Reminder.sample());
    System.out.println("Jackson 3: " + json);

    assertEquals(ISO_JSON, json);
  }

  @Test
  void plainJsonMapperReadsIsoStrings() {
    ObjectMapper mapper = new JsonMapper();

    Reminder reminder = mapper.readValue(ISO_JSON, Reminder.class);

    assertEquals(Reminder.sample(), reminder);
  }

  @Test
  void jsonFormatStillWorks() {
    ObjectMapper mapper = new JsonMapper();

    String json = mapper.writeValueAsString(FormattedReminder.sample());
    System.out.println("Jackson 3 @JsonFormat: " + json);

    assertEquals("{\"title\":\"Pay rent\",\"dueOn\":\"01-10-2026\",\"dueAt\":\"01-10-2026 09:30\"}", json);
  }
}
