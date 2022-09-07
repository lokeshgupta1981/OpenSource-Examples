package com.howtodoinjava.app.xmlunit;

import org.junit.jupiter.api.Test;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.DefaultNodeMatcher;
import org.xmlunit.diff.ElementSelectors;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static org.xmlunit.assertj3.XmlAssert.assertThat;

public class XmlAssertExamples {

  private String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n" +
      "<widget>\n" +
      "  <debug>on</debug>\n" +
      "  <window>\n" +
      "    <title>Widget Title</title>\n" +
      "    <name>Widget Name</name>\n" +
      "    <width>500</width>\n" +
      "    <height>500</height>\n" +
      "    <locations>\n" +
      "      <location>header</location>\n" +
      "      <location>footer</location>\n" +
      "    </locations>\n" +
      "  </window>\n" +
      "</widget>";

  @Test
  void simpleUse() throws URISyntaxException, MalformedURLException {

    //assertThat(xml).isValid();

    /*StreamSource xsd = new StreamSource(new File("schema.xsd"));
    assertThat(xml).isValidAgainst(xsd);*/

    String testXml = "<widget><id>1</id><name>demo</name></widget>";
    String identicalXml = "<widget><id>1</id><name>demo</name></widget>";
    String similarXml = "<widget><name>demo</name><id>1</id></widget>";

    assertThat(testXml).and(identicalXml).areIdentical();
    assertThat(testXml).and(similarXml).areNotIdentical();

    assertThat(testXml).and(similarXml)
        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
        .areSimilar();

    assertThat(testXml).and("<widget><id>1</id></widget>")
        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
        .areNotSimilar();


    assertThat(xml).hasXPath("//widget/debug");
    assertThat(xml).doesNotHaveXPath("//widget/debug/test");
    assertThat(xml).nodesByXPath("//widget/debug").exist();

    //assertThat(xml).isValidAgainst(new URL("schema.xsd"));

    assertThat(xml).valueByXPath("//widget/debug").isEqualTo("on");
    assertThat(xml).valueByXPath("//widget/window/title").isNotBlank();
    assertThat(xml).valueByXPath("count(//widget/window/locations/location)").isEqualTo(2);

    assertThat(testXml).and(identicalXml).areIdentical();
    assertThat(testXml).and(similarXml).areNotIdentical();
    //assertThat(testXml).and(nonIdenticalXml).areSimilar();
    assertThat(testXml).and(similarXml)
        .normalizeWhitespace()
        .ignoreComments()
        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
        .areSimilar();
  }
}
