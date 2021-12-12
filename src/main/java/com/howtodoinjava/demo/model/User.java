package com.howtodoinjava.demo.model;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
  private Long id;
  private String firstName;
  private String lastName;
  private LocalDate dateOfbirth;
  private Department department;
}
