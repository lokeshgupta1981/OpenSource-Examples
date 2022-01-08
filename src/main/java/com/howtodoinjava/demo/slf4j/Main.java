package com.howtodoinjava.demo.slf4j;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.howtodoinjava.demo.lombok.Article;

public class Main {
	public static void main(final String[] args) {

		System.setProperty("ENV", "prod");

		Logger logger = LoggerFactory.getLogger(Main.class);

		MDC.put("MDC_KEY", "VALUE");

		logger.debug("Debug Message Logged !!!");
		logger.info("Info Message Logged !!!");
		logger.error("Error Message Logged !!!", new NullPointerException("Something is NULL"));

		Article a = Article.builder(1L).title("Test Article").tag("Data").build();
		logger.info("Article fecthed for id : {} is : {}", 1, a);

		//Data masking example
		Map<String, String> customer = new HashMap<String, String>();

		customer.put("id", "12345");
		customer.put("ssn", "856-45-6789");
		customer.put("email", "admin@email.com");

		logger.info("Customer found with : {}", new JSONObject(customer));
	}
}
