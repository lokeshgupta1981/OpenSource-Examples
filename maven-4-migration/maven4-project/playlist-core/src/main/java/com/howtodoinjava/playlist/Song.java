package com.howtodoinjava.playlist;

import java.util.Objects;

public record Song(String title, int seconds) {

  public Song {
    Objects.requireNonNull(title, "title");
    if (title.isBlank()) {
      throw new IllegalArgumentException("title must not be blank");
    }
    if (seconds <= 0) {
      throw new IllegalArgumentException("seconds must be positive: " + seconds);
    }
  }
}
