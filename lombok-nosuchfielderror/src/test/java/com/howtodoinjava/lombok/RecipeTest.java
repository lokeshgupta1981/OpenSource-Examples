package com.howtodoinjava.lombok;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RecipeTest {

  @Test
  void dataGeneratesGettersSettersAndToString() {
    Recipe recipe = new Recipe();
    recipe.setName("Pancakes");
    recipe.setMinutes(20);

    assertEquals("Pancakes", recipe.getName());
    assertEquals(20, recipe.getMinutes());
    assertEquals("Recipe(name=Pancakes, minutes=20)", recipe.toString());
  }
}
