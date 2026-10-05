package com.howtodoinjava.playlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class PlaylistTest {

  @Test
  void sumsSongLengths() {
    Playlist playlist = new Playlist("Morning")
        .add(new Song("Intro", 185))
        .add(new Song("Sunrise", 240))
        .add(new Song("Coffee", 120));

    assertEquals(545, playlist.totalSeconds());
    assertEquals("9:05", playlist.length());
  }

  @Test
  void emptyPlaylistHasZeroLength() {
    assertEquals("0:00", new Playlist("Empty").length());
  }

  @Test
  void rejectsInvalidSongs() {
    assertThrows(IllegalArgumentException.class, () -> new Song("Intro", 0));
    assertThrows(IllegalArgumentException.class, () -> new Song(" ", 120));
    assertThrows(NullPointerException.class, () -> new Playlist("Morning").add(null));
  }
}
