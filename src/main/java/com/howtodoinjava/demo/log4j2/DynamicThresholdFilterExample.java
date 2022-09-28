package com.howtodoinjava.demo.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;

public class DynamicThresholdFilterExample {

  private static final Logger logger = LogManager.getLogger(DynamicThresholdFilterExample.class);

  public static void main(String[] args) {

    logger.info("User accessed the page without USER_ROLE = NONE");
    logger.debug("User accessed the page without USER_ROLE = NONE");
    logger.error("User accessed the page without USER_ROLE = NONE");

    ThreadContext.put("USER_ROLE", "ADMIN");
    logger.info("User accessed the page with USER_ROLE = ADMIN");
    logger.debug("User accessed the page with USER_ROLE = ADMIN");
    logger.error("User accessed the page with USER_ROLE = ADMIN");

    ThreadContext.put("USER_ROLE", "GUEST");
    logger.info("User accessed the page with USER_ROLE = GUEST");
    logger.debug("User accessed the page with USER_ROLE = GUEST");
    logger.error("User accessed the page with USER_ROLE = GUEST");

    ThreadContext.put("USER_ROLE", "UNKNOWN");
    logger.info("User accessed the page with USER_ROLE = UNKNOWN");
    logger.debug("User accessed the page with USER_ROLE = UNKNOWN");
    logger.error("User accessed the page with USER_ROLE = UNKNOWN");
  }

}
