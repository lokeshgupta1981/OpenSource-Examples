package com.howtodoinjava.demo.gson;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Writes a LocalDate as "dd/MM/yyyy" and reads it back.
 */
public class LocalDateAdapter extends TypeAdapter<LocalDate> {

  private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  @Override
  public void write(JsonWriter out, LocalDate value) throws IOException {
    if (value == null) {
      out.nullValue();
      return;
    }
    out.value(FORMAT.format(value));
  }

  @Override
  public LocalDate read(JsonReader in) throws IOException {
    if (in.peek() == JsonToken.NULL) {
      in.nextNull();
      return null;
    }
    return LocalDate.parse(in.nextString(), FORMAT);
  }
}
