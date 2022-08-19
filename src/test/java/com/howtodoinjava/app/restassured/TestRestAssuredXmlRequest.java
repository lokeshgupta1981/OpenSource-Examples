package com.howtodoinjava.app.restassured;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class TestRestAssuredXmlRequest {

  @BeforeAll
  public static void setup() {
    baseURI = "https://gorest.co.in/public/v2";
  }

  @Test
  public void createUserWithJSONObject_thenSuccess() throws JSONException, JsonProcessingException {
    UserObject newUser = new UserObject();

    newUser.setName("lokesh");
    newUser.setEmail("admin@howtodoinjava.com");
    newUser.setGender("male");
    newUser.setStatus("active");

    String xml = new XmlMapper().writeValueAsString(newUser);

    System.out.println(xml);

    given()
        .body(xml)
        .contentType("application/xml")
        .queryParam("access-token", "7c73b5c9a897be3fe0642ca6494f4664ee32a7acdb0bb889e6667748e3228375")
        .when()
        .post("/users")
        .then()
        .statusCode(201)
        .body("id", notNullValue())
        .body("name", equalTo("lokesh"))
        .body("gender", equalTo("male"))
        .body("status", equalTo("active"))
        .body("email", equalTo("admin@howtodoinjava.com"))
        .log().all();
  }
}

@JacksonXmlRootElement(localName = "object")
class UserObject {
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Integer id;
  private String name;
  private String email;
  private String gender;
  private String status;


  public UserObject() {
  }

  public UserObject(Integer id, String name, String email, String gender,
                    String status) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.gender = gender;
    this.status = status;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

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

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
