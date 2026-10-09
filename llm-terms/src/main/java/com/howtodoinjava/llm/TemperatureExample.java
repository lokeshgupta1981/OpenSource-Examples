package com.howtodoinjava.llm;

import java.util.Arrays;

public class TemperatureExample {

  // Made-up scores (logits) a model might give to the next word after
  // "The coffee is ..."; real models score every token in their vocabulary
  static final String[] WORDS = {"hot", "ready", "strong", "purple"};
  static final double[] LOGITS = {4.0, 3.0, 2.5, 0.5};

  static double[] softmax(double[] logits, double temperature) {
    double[] scaled = Arrays.stream(logits).map(l -> Math.exp(l / temperature)).toArray();
    double sum = Arrays.stream(scaled).sum();
    return Arrays.stream(scaled).map(s -> s / sum).toArray();
  }

  public static void main(String[] args) {
    for (double t : new double[] {0.2, 1.0, 2.0}) {
      double[] p = softmax(LOGITS, t);
      StringBuilder line = new StringBuilder("T=" + t + "  ");
      for (int i = 0; i < WORDS.length; i++) {
        line.append(String.format("%s %.0f%%  ", WORDS[i], p[i] * 100));
      }
      System.out.println(line.toString().trim());
    }
  }
}
