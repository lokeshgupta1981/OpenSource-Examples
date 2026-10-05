package com.howtodoinjava.playlist.app;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PlaylistAppTest {

  @Test
  void printsSummary() {
    assertEquals("Morning: 3 songs, 9:05", PlaylistApp.summary(PlaylistApp.morningPlaylist()));
  }
}
