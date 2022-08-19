package com.howtodoinjava.app.restassured;

import io.restassured.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class TestRestAssured {

  @BeforeAll
  public static void setup() {
    baseURI = "https://reqres.in/";
    basePath = "/api";
  }

  @Test
  public void createUserWithJSONObject_thenSuccess() throws JSONException {
    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "lokesh");
    requestParams.put("email", "admin@howtodoinjava.com");

    given()
        .body(requestParams.toString())
        .contentType("application/json")
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("name", equalTo("lokesh"))
        .body("email", equalTo("admin@howtodoinjava.com"))
        .log().body();
  }

  @Test
  public void createUserAndExtractResponse_thenSuccess() throws JSONException {
    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "lokesh");
    requestParams.put("email", "admin@howtodoinjava.com");

    String id = given()
        .body(requestParams.toString())
        .contentType("application/json")
        .when()
        .post("/users")
        .then()
        .statusCode(equalTo(201))
        .log().body()
        .extract().body().path("id");

    Assertions.assertNotNull(id);
  }

  @Test
  public void createUserWithCustomObject_thenSuccess() throws JSONException {
    given()
        .body(new User("lokesh", "admin@howtodoinjava.com"))
        .header(new Header("x-custom-hreader", "value"))
        .contentType("application/json")
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        //.header("x-response-header", equalTo("value"))
        .body("id", notNullValue())
        .body("name", equalTo("lokesh"))
        .body("email", equalTo("admin@howtodoinjava.com"))
        .log().body();
  }

  @Test
  public void updateUser_thenSuccess() {
    given()
        .body(new User("john", "john@howtodoinjava.com"))
        .contentType("application/json")
        .when()
        .put("/users/2")
        .then()
        .statusCode(200)
        .body("name", equalTo("john"))
        .body("email", equalTo("john@howtodoinjava.com"))
        .log().body();
  }

  @Test
  public void getUser_thenSuccess() {
    given()
        .pathParam("id", 2)
        .when()
        .get("/users/{id}")
        .then()
        .statusCode(equalTo(200))
        .body("data.id", equalTo(2))
        .body("data.email", equalTo("janet.weaver@reqres.in"));
  }

  @Test
  public void deleteUser_thenSuccess() {
    when()
        .delete("/users/2")
        .then()
        .statusCode(204)
        .body(isEmptyOrNullString());
  }

  @Test
  public void getAllUsers_WithinTime() {
    when()
        .get("/users")
        .then()
        .statusCode(200)
        .body(notNullValue())
        .time(lessThan(2L), TimeUnit.SECONDS);
  }


  @Test
  public void getInvalidUser_thenFailWith404() {
    when()
        .get("/users/25")
        .then()
        .statusCode(404);
  }
}


class User {
  private String name;
  private String email;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User() {
  }

  public User(String name, String email) {
    this.name = name;
    this.email = email;
  }
}