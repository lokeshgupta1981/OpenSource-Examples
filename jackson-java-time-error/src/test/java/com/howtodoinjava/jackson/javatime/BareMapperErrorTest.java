package com.howtodoinjava.jackson.javatime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Reproduces the error with an ObjectMapper that has no JavaTimeModule.
 */
class BareMapperErrorTest {

  private final ObjectMapper mapper = JavaTimeMappers.bareMapper();

  @Test
  void serializingLocalDateTimeFails() {
    InvalidDefinitionException ex = assertThrows(InvalidDefinitionException.class,
        () -> mapper.writeValueAsString(Reminder.sample()));

    System.out.println("writeValueAsString: " + ex.getMessage());
    assertTrue(ex.getMessage().startsWith(
        "Java 8 date/time type `java.time.LocalDate` not supported by default: "
            + "add Module \"com.fasterxml.jackson.datatype:jackson-datatype-jsr310\" to enable handling"));
  }

  @Test
  void eachJavaTimeTypeNamesItselfInTheMessage() {
    record OnlyDateTime(LocalDateTime dueAt) {}
    record OnlyInstant(Instant createdAt) {}

    InvalidDefinitionException dateTime = assertThrows(InvalidDefinitionException.class,
        () -> mapper.writeValueAsString(new OnlyDateTime(LocalDateTime.of(2026, 10, 1, 9, 30))));
    InvalidDefinitionException instant = assertThrows(InvalidDefinitionException.class,
        () -> mapper.writeValueAsString(new OnlyInstant(Instant.parse("2026-09-25T08:15:30Z"))));

    assertTrue(dateTime.getMessage().contains("`java.time.LocalDateTime` not supported by default"));
    assertTrue(instant.getMessage().contains("`java.time.Instant` not supported by default"));
  }

  @Test
  void deserializingFailsToo() {
    String json = "{\"title\":\"Pay rent\",\"dueOn\":\"2026-10-01\","
        + "\"dueAt\":\"2026-10-01T09:30:00\",\"createdAt\":\"2026-09-25T08:15:30Z\"}";

    InvalidDefinitionException ex = assertThrows(InvalidDefinitionException.class,
        () -> mapper.readValue(json, Reminder.class));

    System.out.println("readValue: " + ex.getMessage());
    assertTrue(ex.getMessage().startsWith("Java 8 date/time type `java.time.LocalDate` not supported by default"));
  }

  @Test
  void convertValueFailsToo() {
    // convertValue() wraps the Jackson exception in an IllegalArgumentException
    IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
        () -> mapper.convertValue(Reminder.sample(), Map.class));

    System.out.println("convertValue: " + ex.getMessage());
    assertTrue(ex.getMessage().contains("not supported by default"));
    assertEquals(InvalidDefinitionException.class, ex.getCause().getClass());
  }

  @Test
  void theExceptionTypeIsInvalidDefinitionException() {
    Exception ex = assertThrows(Exception.class, () -> mapper.writeValueAsString(LocalDate.of(2026, 10, 1)));
    assertEquals("com.fasterxml.jackson.databind.exc.InvalidDefinitionException", ex.getClass().getName());
  }
}
