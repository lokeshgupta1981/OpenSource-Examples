package com.howtodoinjava.app.restassured;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.path.json.mapper.factory.GsonObjectMapperFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;
import static io.restassured.RestAssured.*;
import static io.restassured.config.ObjectMapperConfig.objectMapperConfig;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class RestAssuredPostExamples {

  @BeforeAll
  public static void setup() {
    baseURI = "https://gorest.co.in/public/v2";
    RestAssured.config = RestAssuredConfig.config()
        .objectMapperConfig(objectMapperConfig().gsonObjectMapperFactory(

            new GsonObjectMapperFactory() {
              @Override
              public Gson create(Type type, String s) {
                return new GsonBuilder()
                    .setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES)
                    .create();
              }
            }
        ));
  }

  @Test
  public void createUserWithJSONObject_thenSuccess() throws JSONException {
    JSONObject requestParams = new JSONObject();
    requestParams.put("name", "lokesh");
    requestParams.put("email", "admin@howtodoinjava.com");
    requestParams.put("gender", "male");
    requestParams.put("status", "active");

    given()
        .body(requestParams.toString())
        .accept("application/json")
        .contentType("application/json")
        .queryParam("access-token",
            "7c73b5c9a897be3fe0642ca6494f4664ee32a7acdb0bb889e6667748e3228375")
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("name", equalTo("lokesh"))
        .body("gender", equalTo("male"))
        .body("status", equalTo("active"))
        .body("email", equalTo("admin@howtodoinjava.com"))
        .log().body();
  }
}
