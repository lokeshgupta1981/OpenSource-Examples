package com.howtodoinjava.demo.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class MarkerFilterDemo {

  private static final Logger logger = LogManager.getLogger(MarkerFilterDemo.class);

  public static final Marker TRANSACTION = MarkerManager.getMarker("TRANSACTION");
  public static final Marker PAYMENT = MarkerManager.getMarker("PAYMENT");
  public static final Marker USER_MGMT = MarkerManager.getMarker("USER_MGMT");

  static {
    PAYMENT.addParents(TRANSACTION);
  }

  public static void main(String[] args) {
    logger.info(TRANSACTION, "New transaction message!");
    logger.info(PAYMENT, "New payment message!");
    logger.info(USER_MGMT, "New user message!");
  }
}
