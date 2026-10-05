package com.howtodoinjava.playlist.app;

import com.howtodoinjava.playlist.Playlist;
import com.howtodoinjava.playlist.Song;

public class PlaylistApp {

  public static Playlist morningPlaylist() {
    return new Playlist("Morning")
        .add(new Song("Intro", 185))
        .add(new Song("Sunrise", 240))
        .add(new Song("Coffee", 120));
  }

  public static String summary(Playlist playlist) {
    return "%s: %d songs, %s".formatted(playlist.name(), playlist.songs().size(), playlist.length());
  }

  public static void main(String[] args) {
    System.out.println(summary(morningPlaylist()));
  }
}
