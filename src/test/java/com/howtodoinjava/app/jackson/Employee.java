package com.howtodoinjava.app.jackson;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

  private Long id;
  private String name;

  //@JsonFormat(shape = JsonFormat.Shape.NUMBER)
//  @JsonSerialize(using = CustomBooleanSerializer.class)
//  @JsonDeserialize(using = CustomBooleanDeserializer.class)
  private boolean active;

  //@JsonFormat(shape = JsonFormat.Shape.NUMBER)
  /*@JsonSerialize(using = CustomBooleanSerializer.class)
  @JsonDeserialize(using = CustomBooleanDeserializer.class)*/
  private boolean workFromHome;

  public Employee() {
  }

  public Employee(Long id, String name, boolean active, boolean workFromHome) {
    this.id = id;
    this.name = name;
    this.active = active;
    this.workFromHome = workFromHome;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isWorkFromHome() {
    return workFromHome;
  }

  public void setWorkFromHome(boolean workFromHome) {
    this.workFromHome = workFromHome;
  }

  @Override
  public String toString() {
    return "Employee{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", active=" + active +
        ", workFromHome=" + workFromHome +
        '}';
  }
}
