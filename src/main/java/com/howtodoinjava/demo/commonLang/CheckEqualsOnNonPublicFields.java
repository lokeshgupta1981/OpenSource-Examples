package com.howtodoinjava.demo.commonLang;

import java.io.Serializable;
import java.util.List;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class CheckEqualsOnNonPublicFields {

  public static void main(String[] args) {

    Person p1 = new Person(1L, "TestName1", "TestEmail1", "TestPhone1");
    Person p2 = new Person(2L, "TestName2", "TestEmail2", "TestPhone2");
    Person p3 = new Person(1L, "TestName1", "TestEmail1", "TestPhone1");

    boolean result = EqualsBuilder.reflectionEquals(p1, p2);
    System.out.println("p1 equals p2 :: " + result);

    result = EqualsBuilder.reflectionEquals(p1, p3);
    System.out.println("p1 equals p3 :: " + result);

    //
    p3 = new Person(1L, "TestName1", "TestEmail1", "TestPhone1");
    p2 = new Person(2L, "TestName1", "TestEmail1", "TestPhone1");

    List<String> excludeFields = List.of("id");
    result = EqualsBuilder.reflectionEquals(p3, p2, excludeFields);
    System.out.println("p1 equals p2 :: " + result);

    //Inheritance
    ChildVo child1 = new ChildVo(1L, "TestName1", "TestEmail1", "TestPhone1");
    ChildVo child2 = new ChildVo(2L, "TestName1", "TestEmail1", "TestPhone1");

    result = new EqualsBuilder()
      .setReflectUpToClass(ChildVo.class)
      .reflectionAppend(child1, child2)
      .isEquals();

    System.out.println(result);
  }

  static class Person {

    private Long id;
    private String name;
    private String email;
    private String phone;

    public Person(Long id, String name, String email, String phone) {
      this.id = id;
      this.name = name;
      this.email = email;
      this.phone = phone;
    }
  }

  static class BaseVo implements Serializable {

    private Long id;

    public BaseVo(Long id) {
      this.id = id;
    }
  }

  static class ChildVo extends BaseVo implements Serializable {

    private String name;
    private String email;
    private String phone;

    public ChildVo(Long id, String name, String email, String phone) {
      super(id);
      this.name = name;
      this.email = email;
      this.phone = phone;
    }
  }
}

