package com.howtodoinjava.demo.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
  public static void main(final String[] args)
  {
      Logger logger = LoggerFactory.getLogger(Main.class);
      logger.info("Hello World !!");
  }
}
