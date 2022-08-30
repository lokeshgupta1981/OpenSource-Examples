package com.howtodoinjava.app.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateExamples {

  //=========Serializations=============

  //java.util.Date

  @Test
  void testDefaultSerialization_ThenSuccess() throws JsonProcessingException {
    Message message = new Message(1L, "test");
    message.setDate(new Date(1661518473905L));
    message.setLocalDateTime(LocalDateTime.of(2022,8,30,14,16,20,0));


    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JavaTimeModule());
    String json = jsonMapper.writeValueAsString(message);

    System.out.println(json);

    Assertions.assertEquals("{\"id\":1,\"text\":\"test\"," +
        "\"date\":1661518473905,\"localDateTime\":[2022,8,30,14,16,20]}", json);
  }

  @Test
  void testCustomFormat_ThenSuccess() throws JsonProcessingException {
    Message message = new Message(1L, "test");
    message.setDate(new Date(1661518473905L));
    message.setLocalDateTime(LocalDateTime.of(2022, 1, 1, 15, 30));

    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JavaTimeModule());
    jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    String json = jsonMapper.writeValueAsString(message);

    Assertions.assertEquals("{\"id\":1,\"text\":\"test\"," +
        "\"date\":\"2022-08-26 18:24:33\"," +
        "\"localDateTime\":\"2022-01-01T15:30:00\"}", json);
  }

  @Test
  void testCustomSerializer_ThenSuccess() throws JsonProcessingException {
    Message message = new Message(1L, "test");
    message.setLocalDateTime(LocalDateTime.of(2022, 1, 1, 15, 30));

    JsonMapper jsonMapper = new JsonMapper();

    SimpleModule module = new SimpleModule();
    module.addSerializer(LocalDateTime.class,
        new CustomLocalDateTimeSerializer());

    jsonMapper.registerModule(module);

    String json = jsonMapper.writeValueAsString(message);

    Assertions.assertEquals("{\"id\":1,\"text\":\"test\"," +
        "\"localDateTime\":\"2022-01-01 15:30:00\"}", json);
  }

  //=========De-serializations=============

  //java.util.Date

  @Test
  void testDefaultDeserialization_ThenSuccess() throws JsonProcessingException {

    String json = "{\"id\":1,\"text\":\"test\",\"date\":1661518473905," +
        "\"localDateTime\":[2022,8,30,14,16,20]}";

    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JavaTimeModule());
    Message message = jsonMapper.readValue(json, Message.class);

    Assertions.assertEquals(1661518473905L, message.getDate().getTime());
    Assertions.assertEquals(LocalDateTime.of(2022,8,30,14,16,20,0), message.getLocalDateTime());
  }

  @Test
  void testCustomDateFormatDeserialization_ThenSuccess() throws JsonProcessingException {

    String json = "{\"id\":1,\"text\":\"test\",\"date\":1661518473905," +
        "\"localDateTime\":[2022,8,30,14,16,20]}";

    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.registerModule(new JavaTimeModule());
    jsonMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    Message message = jsonMapper.readValue(json, Message.class);

    Assertions.assertEquals(1661518473905L, message.getDate().getTime());
    Assertions.assertEquals("2022-08-30T14:16:20", message.getLocalDateTime().toString());
  }

  @Test
  void testCustomLocalDateTimeDeserializer_ThenSuccess() throws JsonProcessingException {

    String json = "{\"id\":1,\"text\":\"test\",\"localDateTime\":\"1986-04-08 12:30\"}";

    JsonMapper jsonMapper = new JsonMapper();

    SimpleModule module = new SimpleModule();
    module.addDeserializer(LocalDateTime.class, new CustomLocalDateTimeDeserializer());
    jsonMapper.registerModule(module);

    Message message = jsonMapper.readValue(json, Message.class);

    Assertions.assertEquals("1986-04-08T12:30",
        message.getLocalDateTime().toString());
  }

}

class CustomLocalDateTimeDeserializer extends StdDeserializer<LocalDateTime> {

  private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  public CustomLocalDateTimeDeserializer() {
    this(null);
  }

  public CustomLocalDateTimeDeserializer(Class<?> vc) {
    super(vc);
  }

  @Override
  public LocalDateTime deserialize(JsonParser jsonparser,
                                   DeserializationContext context)
      throws IOException {
    String date = jsonparser.getText();
    return LocalDateTime.parse(date, formatter);
  }
}

class CustomLocalDateTimeSerializer
    extends StdSerializer<LocalDateTime> {

  private static DateTimeFormatter formatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  public CustomLocalDateTimeSerializer() {
    this(null);
  }

  public CustomLocalDateTimeSerializer(Class<LocalDateTime> t) {
    super(t);
  }

  @Override
  public void serialize(
      LocalDateTime value,
      JsonGenerator gen,
      SerializerProvider arg2)
      throws IOException, JsonProcessingException {

    gen.writeString(formatter.format(value));
  }
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class Message {
  private Long id;
  private String text;

  //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy
  // hh:mm:ss")
  private Date date;

  //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime localDateTime;

  public Message() {
  }

  public Message(Long id, String text) {
    this.id = id;
    this.text = text;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public LocalDateTime getLocalDateTime() {
    return localDateTime;
  }

  public void setLocalDateTime(LocalDateTime localDateTime) {
    this.localDateTime = localDateTime;
  }
}
