package com.howtodoinjava.playlist.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class PlaylistAppIT {

  @Test
  void mainPrintsSummary() {
    PrintStream original = System.out;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try (PrintStream capture = new PrintStream(out, true, StandardCharsets.UTF_8)) {
      System.setOut(capture);
      PlaylistApp.main(new String[0]);
    } finally {
      System.setOut(original);
    }
    assertEquals("Morning: 3 songs, 9:05", out.toString(StandardCharsets.UTF_8).strip());
  }
}
