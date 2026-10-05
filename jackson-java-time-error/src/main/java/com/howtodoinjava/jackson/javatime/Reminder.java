package com.howtodoinjava.jackson.javatime;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A reminder with the three java.time types that trigger the error most often.
 */
public record Reminder(String title, LocalDate dueOn, LocalDateTime dueAt, Instant createdAt) {

  public static Reminder sample() {
    return new Reminder("Pay rent",
        LocalDate.of(2026, 10, 1),
        LocalDateTime.of(2026, 10, 1, 9, 30),
        Instant.parse("2026-09-25T08:15:30Z"));
  }
}
