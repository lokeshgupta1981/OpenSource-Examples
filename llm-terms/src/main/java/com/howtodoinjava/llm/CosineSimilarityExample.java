package com.howtodoinjava.llm;

public class CosineSimilarityExample {

  // Made-up 4-dimensional vectors for illustration; real embedding models return
  // hundreds or thousands of dimensions
  static final float[] CAT = {0.9f, 0.8f, 0.1f, 0.0f};
  static final float[] KITTEN = {0.85f, 0.75f, 0.2f, 0.05f};
  static final float[] INVOICE = {0.05f, 0.1f, 0.9f, 0.8f};

  static double cosineSimilarity(float[] a, float[] b) {
    double dot = 0, normA = 0, normB = 0;
    for (int i = 0; i < a.length; i++) {
      dot += a[i] * b[i];
      normA += a[i] * a[i];
      normB += b[i] * b[i];
    }
    return dot / (Math.sqrt(normA) * Math.sqrt(normB));
  }

  public static void main(String[] args) {
    System.out.printf("cat vs kitten:  %.3f%n", cosineSimilarity(CAT, KITTEN));
    System.out.printf("cat vs invoice: %.3f%n", cosineSimilarity(CAT, INVOICE));
  }
}
