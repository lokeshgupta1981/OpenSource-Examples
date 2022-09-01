package com.howtodoinjava.app.jackson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class XmlToJsonAndViceVersa {

  //{"id":1,"name":"Lokesh"}
  //<Data><id>1</id><name>Lokesh</name></Data>

  @Test
  public void testXmlToJson() throws IOException {
    String xml = "<Data><id>1</id><name>Lokesh</name></Data>";

    XmlMapper xmlMapper = new XmlMapper();
    Data dataInstance = xmlMapper.readValue(xml.getBytes(), Data.class);

    JsonMapper jsonMapper = new JsonMapper();
    String json = jsonMapper.writeValueAsString(dataInstance);

    Assertions.assertEquals("{\"id\":1,\"name\":\"Lokesh\"}", json);
  }

  @Test
  public void testXmlToJsonUsingObjectNode() throws IOException {
    String xml = "<Data><id>1</id><name>Lokesh</name></Data>";

    XmlMapper xmlMapper = new XmlMapper();
    ObjectNode dataInstance = xmlMapper.readValue(xml.getBytes(), ObjectNode.class);

    JsonMapper jsonMapper = new JsonMapper();
    String json = jsonMapper.writeValueAsString(dataInstance);

    Assertions.assertEquals("{\"id\":\"1\",\"name\":\"Lokesh\"}", json);
  }

  @Test
  public void testJsonToXml() throws JsonProcessingException {

    String json = "{\"id\": 1,\"name\": \"Lokesh\"}";

    JsonMapper jsonMapper = new JsonMapper();
    Data dataInstance = jsonMapper.readValue(json, Data.class);

    XmlMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(dataInstance);

    Assertions.assertEquals("<Data><id>1</id><name>Lokesh</name></Data>", xml);
  }

  @Test
  public void testJsonToXmlUsingJsonNode() throws JsonProcessingException {

    String json = "{\"id\": 1,\"name\": \"Lokesh\"}";

    JsonMapper jsonMapper = new JsonMapper();
    ObjectNode objectNode = jsonMapper.readValue(json, ObjectNode.class);

    XmlMapper xmlMapper = new XmlMapper();
    String xml = xmlMapper.writeValueAsString(objectNode);

    Assertions.assertEquals("<ObjectNode><id>1</id><name>Lokesh</name></ObjectNode>", xml);
  }
}

@lombok.Data
class Data {
  private Long id;
  private String name;
}