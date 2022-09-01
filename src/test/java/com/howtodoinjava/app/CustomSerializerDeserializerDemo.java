package com.howtodoinjava.app;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CustomSerializerDeserializerDemo {
  @Test
  void testDefaultSerialization() throws JsonProcessingException {
    ZonedDateTime zdt = ZonedDateTime
        .of(LocalDateTime.of(2022, 1, 1, 1, 1), ZoneId.of("GMT"));

    Record record = new Record(1L, "test-message", zdt,
        new ArchiveStatus(true));

    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JavaTimeModule());

    String json = jsonMapper.writeValueAsString(record);

    System.out.println(json);

    Assertions.assertEquals("{\"id\":1,\"message\":\"test-message\"," +
        "\"timestamp\":1640998860.000000000,\"status\":{\"active\":true}}",
        json);
  }

  @Test
  void testCustomSerialization() throws JsonProcessingException {
    ZonedDateTime zdt = ZonedDateTime
        .of(LocalDateTime.of(2022, 1, 1, 1, 1), ZoneId.of("GMT"));

    Record record = new Record(1L, "test-message", zdt,
        new ArchiveStatus(true));

    JsonMapper jsonMapper = new JsonMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addSerializer(Record.class, new RecordSerializer());
    jsonMapper.registerModule(simpleModule);

    String json = jsonMapper.writeValueAsString(record);

    Assertions.assertEquals("{\"id\":1,\"message\":\"test-message\"," +
        "\"timestamp\":\"2022-01-01 01:00:01 AM GMT\",\"status\":\"active\"}"
        , json);
  }

  @Test
  void testCustomDeserialization() throws JsonProcessingException {
    ZonedDateTime zdt = ZonedDateTime
        .of(LocalDateTime.of(2022, 1, 1, 1, 1), ZoneId.of("GMT"));

    String json = "{\"id\":1,\"message\":\"test-message\"," +
        "\"timestamp\":\"2022-01-01 01:00:01 AM GMT\",\"status\":\"active\"}";

    JsonMapper jsonMapper = new JsonMapper();
    SimpleModule simpleModule = new SimpleModule();
    simpleModule.addDeserializer(Record.class, new RecordDeserializer());
    jsonMapper.registerModule(simpleModule);

    Record record = jsonMapper.readValue(json, Record.class);

    System.out.println(record);

    Assertions.assertEquals(1L, record.getId());
    Assertions.assertEquals("test-message", record.getMessage());
    Assertions.assertEquals(zdt, record.getTimestamp());
    Assertions.assertEquals(true, record.getStatus().getActive());
  }
}

class RecordSerializer extends StdSerializer<Record> {

  private static DateTimeFormatter dtf
      = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:ss:mm a z", Locale.US);

  protected RecordSerializer() {
    super(Record.class);
  }

  @Override
  public void serialize(Record value, JsonGenerator gen,
                        SerializerProvider serializers) throws IOException {

    gen.writeStartObject();
    gen.writeNumberField("id", value.getId());
    gen.writeStringField("message", value.getMessage());
    gen.writeStringField("timestamp", dtf.format(value.getTimestamp()));
    if (value.getStatus() != null) {
      gen.writeStringField("status", value.getStatus().getActive() ?
          "active" : "inactive");
    }
    gen.writeEndObject();
  }
}

class RecordDeserializer extends StdDeserializer<Record> {

  private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM" +
      "-dd hh:ss:mm a z", Locale.US);

  public RecordDeserializer() {
    this(null);
  }

  public RecordDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public Record deserialize(JsonParser parser, DeserializationContext ctx)
      throws IOException, JacksonException {

    JsonNode node = parser.getCodec().readTree(parser);
    Integer id = (Integer) ((IntNode) node.get("id")).numberValue();
    String message = node.get("message").asText();
    String timestamp = node.get("timestamp").asText();
    ArchiveStatus status = new ArchiveStatus(false);

    if(node.get("status") != null) {
      String active = node.get("status").asText();
      if("active".equalsIgnoreCase(active)) {
        status.setActive(true);
      }
    }

    Record record = new Record(id.longValue(), message, ZonedDateTime.parse(timestamp, dtf),
        status);

    return record;
  }
}

@Data
@AllArgsConstructor
@NoArgsConstructor
/*@JsonSerialize(using = RecordSerializer.class)
@JsonDeserialize(using = RecordDeserializer.class)*/
class Record {
  @JacksonXmlProperty(isAttribute = true)
  private Long id;
  private String message;
  private ZonedDateTime timestamp;
  private ArchiveStatus status;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class ArchiveStatus {
  private Boolean active;
}
