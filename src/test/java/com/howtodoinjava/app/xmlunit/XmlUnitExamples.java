package com.howtodoinjava.app.xmlunit;

import org.apache.commons.collections4.IterableUtils;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import org.xmlunit.builder.DiffBuilder;
import org.xmlunit.builder.Input;
import org.xmlunit.diff.*;
import org.xmlunit.input.CommentLessSource;
import org.xmlunit.input.ElementContentWhitespaceStrippedSource;
import org.xmlunit.input.WhitespaceNormalizedSource;
import org.xmlunit.input.WhitespaceStrippedSource;
import org.xmlunit.matchers.EvaluateXPathMatcher;
import org.xmlunit.matchers.HasXPathMatcher;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.xmlunit.matchers.CompareMatcher.isIdenticalTo;
import static org.xmlunit.matchers.CompareMatcher.isSimilarTo;

public class XmlUnitExamples {

  //Creating Source

  @Test
  void createSource_ThenSuccess() throws ParserConfigurationException, IOException, SAXException {
    //1 - Using File
    Input.from(new File("widget.xml"));
    Input.fromFile("widget.xml");
    Input.fromFile(getClass().getClassLoader().getResource("widget.xml").getPath());

    //2 - Using String
    Input.from("<widget><id>1</id><name>demo</name></widget>");
    Input.fromString("<widget><id>1</id><name>demo</name></widget>");

    //3 - Using Stream
    Input.from(XmlUnitExamples.class.getResourceAsStream("widget.xml"));
    Input.fromStream(XmlUnitExamples.class.getResourceAsStream("widget.xml"));

    //4 - From Reader
    Input.from(new StringReader("<widget><id>1</id><name>demo</name></widget>"));
    Input.fromReader(new StringReader("<widget><id>1</id><name>demo</name></widget>"));

    //5 - From Byte Array
    Input.from("<widget><id>1</id><name>demo</name></widget>".getBytes());
    Input.fromByteArray("<widget><id>1</id><name>demo</name></widget>".getBytes());

    //6 - From Document
    DocumentBuilder b = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document document = b.parse(new File("widget.xml"));
    Input.from(document);
    Input.fromDocument(document);
  }

  @Test
  void normalizedXmlSources_ThenSuccess() throws Exception {
    CommentLessSource commentLessSource
        = new CommentLessSource(Input.fromFile("widget.xml").build());

    WhitespaceStrippedSource whitespaceStrippedSource
        = new WhitespaceStrippedSource(Input.fromFile("widget.xml").build());

    WhitespaceNormalizedSource whitespaceNormalizedSource
        = new WhitespaceNormalizedSource(Input.fromFile("widget.xml").build());

    ElementContentWhitespaceStrippedSource elementContentWhitespaceStrippedSource
        = new ElementContentWhitespaceStrippedSource(Input.fromFile("widget.xml").build());
  }

  @Test
  void normalizedXmlMatchers_ThenSuccess() throws Exception {
    String testXml = "<widget><id>1</id><name>demo</name></widget>";
    String identicalXml = "<widget><id>1</id><name>demo</name></widget>";

    assertThat(testXml, isIdenticalTo(identicalXml).normalizeWhitespace());
    assertThat(testXml, isIdenticalTo(identicalXml).ignoreComments());
  }

  @Test
  void compareIdenticalAndSimilarXmlWithHamcrest_ThenSuccess() throws Exception {
    String testXml = "<widget><id>1</id><name>demo</name></widget>";
    String identicalXml = "<widget><id>1</id><name>demo</name></widget>";
    String nonIdenticalXml = "<widget><name>demo</name><id>1</id></widget>";

    assertThat(testXml, isIdenticalTo(identicalXml));
    assertThat(testXml, not(isIdenticalTo(nonIdenticalXml)));

    assertThat(testXml, isSimilarTo(nonIdenticalXml)
        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName)));

    assertThat(new File("widget.xml"), isIdenticalTo(identicalXml));
  }

  @Test
  void compareOnlySpecificTags_ThenSuccess() {
    String testXml = "<widget><id>1</id><name>test</name></widget>";
    String testXmlWithDiffValues = "<widget><name>live</name><id>1</id></widget>";

    Diff diffs = DiffBuilder.compare(testXml)
        .withTest(testXmlWithDiffValues)
        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
        .withNodeFilter(
            node -> node.getNodeName().equalsIgnoreCase("id")
        )
        .build();

    assertThat(IterableUtils.size(diffs.getDifferences()), equalTo(0));
  }

  @Test
  void compareIdenticalAndSimilarXmlDiff_ThenSuccess() throws Exception {
    String testXml = "<widget><id>1</id><name>demo</name></widget>";
    String identicalXml = "<widget><id>1</id><name>demo</name></widget>";
    String nonIdenticalXml = "<widget><name>demo</name><id>1</id></widget>";

    Diff diffForIdentical = DiffBuilder
        .compare(testXml)
        .withTest(identicalXml)
        .checkForIdentical()
        .build();

    assertThat(IterableUtils.size(diffForIdentical.getDifferences()), equalTo(0));

    Diff diffForSimilarity = DiffBuilder
        .compare(testXml)
        .withTest(nonIdenticalXml)
        .checkForSimilar()
        .withNodeMatcher(new DefaultNodeMatcher(ElementSelectors.byName))
        .build();

    assertThat(IterableUtils.size(diffForSimilarity.getDifferences()), equalTo(0));
  }

  @Test
  void containsAsChildXmlUsingXPath_ThenSuccess() throws Exception {
    String fullXml = "<widget><id>1</id><name>demo</name></widget>";

    assertThat(fullXml, HasXPathMatcher.hasXPath("/widget/id"));
    assertThat(fullXml, EvaluateXPathMatcher.hasXPath("/widget/id/text()", equalTo("1")));
  }
}
