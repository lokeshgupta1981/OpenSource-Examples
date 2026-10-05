package com.howtodoinjava.jackson.javatime;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * The same reminder with a custom date pattern per field through @JsonFormat.
 */
public record FormattedReminder(
    String title,
    @JsonFormat(pattern = "dd-MM-yyyy") LocalDate dueOn,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm") LocalDateTime dueAt) {

  public static FormattedReminder sample() {
    return new FormattedReminder("Pay rent",
        LocalDate.of(2026, 10, 1),
        LocalDateTime.of(2026, 10, 1, 9, 30));
  }
}
