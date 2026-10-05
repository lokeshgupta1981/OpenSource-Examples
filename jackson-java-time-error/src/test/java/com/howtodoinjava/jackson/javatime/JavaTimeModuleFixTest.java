package com.howtodoinjava.jackson.javatime;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * The same values work once JavaTimeModule is registered, in all three registration styles.
 */
class JavaTimeModuleFixTest {

  private static final String ISO_JSON = "{\"title\":\"Pay rent\",\"dueOn\":\"2026-10-01\","
      + "\"dueAt\":\"2026-10-01T09:30:00\",\"createdAt\":\"2026-09-25T08:15:30Z\"}";

  @Test
  void registerModuleWritesTimestampsByDefault() throws Exception {
    ObjectMapper mapper = JavaTimeMappers.withRegisterModule();

    String json = mapper.writeValueAsString(Reminder.sample());
    System.out.println("registerModule: " + json);

    assertEquals("{\"title\":\"Pay rent\",\"dueOn\":[2026,10,1],"
        + "\"dueAt\":[2026,10,1,9,30],\"createdAt\":1790324130.000000000}", json);
  }

  @Test
  void findAndRegisterModulesWorksToo() throws Exception {
    ObjectMapper mapper = JavaTimeMappers.withFindAndRegisterModules();

    String json = mapper.writeValueAsString(Reminder.sample());
    System.out.println("findAndRegisterModules: " + json);
    System.out.println("registered module ids: " + mapper.getRegisteredModuleIds());

    assertEquals("{\"title\":\"Pay rent\",\"dueOn\":[2026,10,1],"
        + "\"dueAt\":[2026,10,1,9,30],\"createdAt\":1790324130.000000000}", json);
  }

  @Test
  void disablingWriteDatesAsTimestampsGivesIsoStrings() throws Exception {
    JsonMapper mapper = JavaTimeMappers.isoMapper();

    String json = mapper.writeValueAsString(Reminder.sample());
    System.out.println("ISO-8601: " + json);

    assertEquals(ISO_JSON, json);
  }

  @Test
  void readsIsoStringsAndTimestampArrays() throws Exception {
    ObjectMapper mapper = JavaTimeMappers.withRegisterModule();

    Reminder fromIso = mapper.readValue(ISO_JSON, Reminder.class);
    Reminder fromArrays = mapper.readValue("{\"title\":\"Pay rent\",\"dueOn\":[2026,10,1],"
        + "\"dueAt\":[2026,10,1,9,30],\"createdAt\":1790324130.000000000}", Reminder.class);

    assertEquals(Reminder.sample(), fromIso);
    assertEquals(Reminder.sample(), fromArrays);
  }

  @Test
  void jsonFormatPatternControlsOneField() throws Exception {
    ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    String json = mapper.writeValueAsString(FormattedReminder.sample());
    System.out.println("@JsonFormat: " + json);
    FormattedReminder back = mapper.readValue(json, FormattedReminder.class);

    assertEquals("{\"title\":\"Pay rent\",\"dueOn\":\"01-10-2026\",\"dueAt\":\"01-10-2026 09:30\"}", json);
    assertEquals(FormattedReminder.sample(), back);
  }

  @Test
  void singleValueWithoutWrapper() throws Exception {
    ObjectMapper mapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    String json = mapper.writeValueAsString(LocalDate.of(2026, 10, 1));
    LocalDate date = mapper.readValue("\"2026-10-01\"", LocalDate.class);

    assertEquals("\"2026-10-01\"", json);
    assertEquals(LocalDate.of(2026, 10, 1), date);
  }
}
