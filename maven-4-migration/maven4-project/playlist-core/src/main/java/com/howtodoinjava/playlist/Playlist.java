package com.howtodoinjava.playlist;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Playlist {

  private final String name;
  private final List<Song> songs = new ArrayList<>();

  public Playlist(String name) {
    this.name = Objects.requireNonNull(name, "name");
  }

  public Playlist add(Song song) {
    songs.add(Objects.requireNonNull(song, "song"));
    return this;
  }

  public String name() {
    return name;
  }

  public List<Song> songs() {
    return List.copyOf(songs);
  }

  public int totalSeconds() {
    return songs.stream().mapToInt(Song::seconds).sum();
  }

  /** Returns the total length as m:ss, for example 9:05. */
  public String length() {
    int total = totalSeconds();
    return "%d:%02d".formatted(total / 60, total % 60);
  }
}
