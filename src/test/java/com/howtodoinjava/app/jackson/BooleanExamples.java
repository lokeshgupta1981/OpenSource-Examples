package com.howtodoinjava.app.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class BooleanExamples {
  Employee employee;

  @BeforeEach
  public void setup(){
    employee = new Employee(1L, "Alex", true, false);
  }

  /*@Test
  void testDefaultSerialization_ThenSuccess() throws JsonProcessingException {
    JsonMapper jsonMapper = new JsonMapper();
    String json = jsonMapper.writeValueAsString(employee);

    Assertions.assertEquals("{\"id\":1,\"name\":\"Alex\",\"active\":true," +
        "\"workFromHome\":false}", json);
  }*/

  /*@Test
  void testSerialization_FieldLevelJsonFormat_ThenSuccess() throws JsonProcessingException {
    JsonMapper jsonMapper = new JsonMapper();
    String json = jsonMapper.writeValueAsString(employee);

    Assertions.assertEquals("{\"id\":1,\"name\":\"Alex\",\"active\":1," +
        "\"workFromHome\":0}", json);
  }*/

  /*@Test
  void testSerialization_GlobalConfig_ThenSuccess() throws JsonProcessingException {
    JsonMapper jsonMapper = new JsonMapper();
    jsonMapper.configOverride(boolean.class)
        .setFormat(JsonFormat.Value.forShape(JsonFormat.Shape.NUMBER));

    String json = jsonMapper.writeValueAsString(employee);

    Assertions.assertEquals("{\"id\":1,\"name\":\"Alex\",\"active\":1," +
        "\"workFromHome\":0}", json);
  }*/

  //Deserialization

  /*@Test
  void testDefaultDeserialization_ThenSuccess() throws JsonProcessingException {

    String json = "{\"id\":1,\"name\":\"Alex\",\"active\":1,\"workFromHome\":0}";

    JsonMapper jsonMapper = new JsonMapper();
    Employee employee = jsonMapper.readValue(json, Employee.class);

    Assertions.assertEquals(employee.isActive(), true);
    Assertions.assertEquals(employee.isWorkFromHome(), false);
  }*/

  @Test
  void testCustomDeserialization_ThenSuccess() throws JsonProcessingException {

    String json = "{\"id\":1,\"name\":\"Alex\",\"active\":\"active\",\"workFromHome\":\"inactive\"}";

    JsonMapper jsonMapper = new JsonMapper();

    SimpleModule module = new SimpleModule();
    module.addDeserializer(boolean.class, new CustomBooleanDeserializer());
    module.addSerializer(boolean.class, new CustomBooleanSerializer());
    jsonMapper.registerModule(module);

    Employee employee = jsonMapper.readValue(json, Employee.class);

    Assertions.assertEquals(employee.isActive(), true);
    Assertions.assertEquals(employee.isWorkFromHome(), false);
  }
}

class CustomBooleanSerializer extends JsonSerializer<Boolean> {
  @Override
  public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers)
      throws IOException {
    gen.writeString(value ? "1" : "0");
  }
}

class CustomBooleanDeserializer extends JsonDeserializer<Boolean> {

  @Override
  public Boolean deserialize(JsonParser p, DeserializationContext ctx) throws IOException {

    if (List.of("1", "active", "true", "enabled").contains(p.getText())) {
      return Boolean.TRUE;
    }
    else if (List.of("0", "inactive", "false", "disabled").contains(p.getText())) {
      return Boolean.FALSE;
    }
    return null;
  }
}
