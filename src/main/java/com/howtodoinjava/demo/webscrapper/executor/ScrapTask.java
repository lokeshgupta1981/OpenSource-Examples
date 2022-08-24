package com.howtodoinjava.demo.webscrapper.executor;

import com.howtodoinjava.demo.webscrapper.WebScrappingApp;
import com.howtodoinjava.demo.webscrapper.ScappedEntity;
import io.restassured.RestAssured;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.http.params.CoreConnectionPNames;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static io.restassured.RestAssured.given;

public class ScrapTask implements Runnable {
  public ScrapTask(ScappedEntity entity) {
    this.entity = entity;
  }

  private ScappedEntity entity;

  public ScappedEntity getEntity() {
    return entity;
  }

  public void setEntity(ScappedEntity entity) {
    this.entity = entity;
  }

  static String[] tokens = new String[]{" private", "pvt.", " pvt",
      " limited", "ltd.", " ltd"};

  static RestAssuredConfig config = RestAssured.config()
      .httpClient(HttpClientConfig.httpClientConfig()
          .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, 30000)
          .setParam(CoreConnectionPNames.SO_TIMEOUT, 30000));

  @Override
  public void run() {
    try {
      loadPagesAndGetTitles(entity.getRootUrl());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadPagesAndGetTitles(String rootUrl) {

    try{
      Response response = given()
          .config(config)
          .when()
          .get(rootUrl)
          .then()
          .log().ifError()
          .contentType(ContentType.HTML).
          extract().response();

      Document document = Jsoup.parse(response.getBody().asString());
      Elements anchors = document.getElementsByTag("a");
      if (anchors != null && anchors.size() > 0) {
        for (int i = 0; i < anchors.size() && i < 20; i++) {
          String visitedUrl = anchors.get(i).attributes().get("href");
          if(visitedUrl.startsWith("/")) {
            String title = getTitle(rootUrl + visitedUrl);

            ScappedEntity newEntity = new ScappedEntity();
            newEntity.setRownum(WebScrappingApp.rowCounter++);
            newEntity.setRootUrl(entity.getRootUrl());
            newEntity.setVisitedUrl(rootUrl + visitedUrl);
            newEntity.setTitle(title);

            System.out.println("Fetched Record: " + newEntity);
            WebScrappingApp.processedRecords.add(newEntity);
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private String getTitle(String url) {
    try {
      Response response = given()
          .config(config)
          .when()
          .get(url)
          .then()
          .log().ifError()
          .contentType(ContentType.HTML).
          extract().response();

      Document document = Jsoup.parse(response.getBody().asString());
      Elements titles = document.getElementsByTag("title");

      if (titles != null && titles.size() > 0) {
        return titles.get(0).text();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return "Not Found";
  }
}
