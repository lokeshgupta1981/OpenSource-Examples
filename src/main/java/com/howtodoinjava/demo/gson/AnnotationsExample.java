package com.howtodoinjava.demo.gson;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @SerializedName renames one field in the JSON, @Expose picks which
 * fields take part when excludeFieldsWithoutExposeAnnotation() is on.
 */
public class AnnotationsExample {

  public static void main(String[] args) {
    Member member = new Member(7, "Lokesh", "lokesh@example.com", "secret");

    // @SerializedName: Java field "fullName" becomes JSON "full_name"
    String json = new Gson().toJson(member);
    System.out.println(json);

    // Reading accepts the main name and every alternate name
    Member fromMain = new Gson().fromJson("{\"id\":8,\"full_name\":\"Alex\"}", Member.class);
    System.out.println(fromMain.getFullName());
    Member fromAlternate = new Gson().fromJson("{\"id\":9,\"name\":\"Maria\"}", Member.class);
    System.out.println(fromAlternate.getFullName());

    // @Expose: only annotated fields are written
    Gson exposeOnly = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    System.out.println(exposeOnly.toJson(member));

    // transient fields are never written, with or without @Expose
    System.out.println(new Gson().toJson(member).contains("password"));
  }

  static class Member {
    @Expose
    private int id;

    @Expose
    @SerializedName(value = "full_name", alternate = {"name", "fullname"})
    private String fullName;

    private String email;

    private transient String password;

    Member(int id, String fullName, String email, String password) {
      this.id = id;
      this.fullName = fullName;
      this.email = email;
      this.password = password;
    }

    String getFullName() {
      return fullName;
    }
  }
}
