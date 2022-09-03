package com.howtodoinjava.app.jsonassert;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.Map;

public class JsonAssertExamples {

  static String json;

  @BeforeAll
  public static void setup() {
    try {
      URL fileUrl = JsonAssertExamples.class.getClassLoader().getResource("widget.json");
      File file = new File(fileUrl.getFile());
      json = new String(Files.readAllBytes(file.toPath()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void testSetup(){
    JSONAssert.assertEquals(
        "{\"widget\":{\"debug\":\"on\",\"window\":{\"title\":\"Client Info\"," +
            "\"name\":\"client_info\"}}}", json, JSONCompareMode.LENIENT);
  }

  @Test
  void testJSONObject(){
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("widget", Map.of("debug", "on"));

    JSONAssert.assertEquals(
        "{\"widget\":{\"debug\":\"on\"}}", jsonObject, JSONCompareMode.LENIENT);
  }

  @Test
  void testJSONArray(){
    JSONArray jsonArray = new JSONArray();
    jsonArray.put(10);
    jsonArray.put(10);
    jsonArray.put(10);
    jsonArray.put(50);

    JSONAssert.assertEquals(
        "[10,10,10,50]", jsonArray, JSONCompareMode.STRICT);
  }

  /*@Test
  void testFailMessage(){
    String message = "Comparison Failed.";
    JSONAssert.assertEquals(message,
        "{\"widget\":{\"debug\":\"on\",\"window\":{\"titles\":\"Client Info\"," +
            "\"name\":\"client_info\"}}}", json, JSONCompareMode.LENIENT);
  }*/
}
