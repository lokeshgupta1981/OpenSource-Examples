package com.howtodoinjava.app.jsonpath;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.Option.*;

public class TestJsonPathExpressions {
  static String json;

  @BeforeAll
  public static void setup() {
    try {
      URL fileUrl = TestJsonPathExpressions.class.getClassLoader().getResource("widget.json");
      File file = new File(fileUrl.getFile());
      json = new String(Files.readAllBytes(file.toPath()));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /*@Test
  public void testGetWidgetTitle_ThenSuccess(){
    String widgetTitle = JsonPath.read(json, "$.widget.window.title");
    Assertions.assertEquals("Client Info", widgetTitle);
  }*/

  /*@Test
  public void testGetTrueDisplayLocations_ThenSuccess(){
    List<String> locations = JsonPath
        .read(json, "$.widget.window.locations[?(@.display == 'true')].name");
    Assertions.assertTrue(List.of("header", "footer").containsAll(locations));
  }*/

  /*@Test
  public void testGetAllDisplayLocations_ThenSuccess(){
    List<String> locations = JsonPath
        .read(json, "$.widget.window.locations[*].name");
    Assertions.assertTrue(List.of("header", "footer", "sidebar").containsAll(locations));
  }*/

  /*@Test
  public void testFirstDisplayLocation_ThenSuccess(){
    List<String> locations = JsonPath
        .read(json, "$.widget.window.locations[0,1].name");
    Assertions.assertTrue(List.of("header", "footer").containsAll(locations));
  }*/

  /*@Test
  public void testLargestPadding_ThenSuccess(){
    Double largestPadding = JsonPath.read(json, "$.widget.window.padding.max()");
    Assertions.assertEquals(50, largestPadding);
  }*/

  /*@Test
  public void testGetTrueDisplayLocationsWithPredicate_ThenSuccess(){

    Predicate displayEnabled = new Predicate() {
      @Override
      public boolean apply(PredicateContext ctx) {
        return ctx.item(Map.class).get("display").toString().equalsIgnoreCase("true");
      }
    };

    List<String> locations = JsonPath.read(json, "$.widget.window.locations[?].name", displayEnabled);
    Assertions.assertTrue(List.of("header", "footer").containsAll(locations));
  }*/

  /*@Test
  public void testConfiguration_ThenSuccess(){

    Configuration configuration = Configuration
        .builder()
        .options(ALWAYS_RETURN_LIST, SUPPRESS_EXCEPTIONS)
        .build();

    List<String> locations = JsonPath
        .using(configuration)
        .parse(json).read("$.widget.window.locations[0].name");
    Assertions.assertTrue(List.of("header").containsAll(locations));
  }*/
}
