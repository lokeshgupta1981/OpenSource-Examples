package com.howtodoinjava.levels.recipes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * A small service that logs at every level while it looks up a recipe.
 */
public class RecipeService {

  private static final Logger logger = LogManager.getLogger(RecipeService.class);

  public String findRecipe(String name) {
    logger.trace("findRecipe() called with name={}", name);
    logger.debug("Looking up recipe {} in the cache", name);
    logger.info("Recipe {} loaded", name);
    logger.warn("Recipe {} has no cooking time", name);
    logger.error("Could not load the image of recipe {}", name);
    logger.fatal("Recipe database is not reachable");
    return name;
  }
}
