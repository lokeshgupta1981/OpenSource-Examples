package com.howtodoinjava.ml;

import smile.classification.LogisticRegression;

public class SmileExample {

  public static void main(String[] args) {
    // Features: weight in grams, length in cm. Labels: 0 = apple, 1 = banana
    double[][] x = {
        {150, 7}, {170, 8}, {140, 7}, {160, 8},
        {120, 18}, {130, 20}, {110, 17}, {125, 19}};
    int[] y = {0, 0, 0, 0, 1, 1, 1, 1};

    LogisticRegression model = LogisticRegression.fit(x, y);

    int fruit1 = model.predict(new double[] {155, 7});
    int fruit2 = model.predict(new double[] {118, 19});
    System.out.println("155 g, 7 cm  -> " + (fruit1 == 0 ? "apple" : "banana"));
    System.out.println("118 g, 19 cm -> " + (fruit2 == 0 ? "apple" : "banana"));
  }
}
