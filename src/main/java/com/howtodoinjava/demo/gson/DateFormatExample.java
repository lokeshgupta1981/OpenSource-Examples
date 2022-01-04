package com.howtodoinjava.demo.gson;

import java.time.LocalDate;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

public class DateFormatExample {
	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(SerializationExample.class);

	    User user = new User(1L, "lokesh", "gupta", new Date());

	    Gson gson = new GsonBuilder()
	      .setPrettyPrinting()
	      .setDateFormat("dd-MM-yyyy")
	      .create();

	    String jsonString = gson.toJson(user);
	    
	    logger.info("Formatted JSON : " + jsonString);
	}
}

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
class User {
  private Long id;
  private String firstName;
  private String lastName;
  private Date dateOfbirth;
}
