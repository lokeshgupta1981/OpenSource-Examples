package com.howtodoinjava.demo.opencsv;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee
{
  private String id;
  private String firstName;
  private String lastName;
  private String country;
  private String age;
}
