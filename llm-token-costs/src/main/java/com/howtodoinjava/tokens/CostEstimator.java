package com.howtodoinjava.tokens;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingType;
import java.util.List;

public class CostEstimator {

  static final List<ModelPrice> PRICES = List.of(
      new ModelPrice("GPT-5.6 Terra", 2.00, 0.20, 12.00),
      new ModelPrice("GPT-5.6 Luna", 0.20, 0.02, 1.20),
      new ModelPrice("Claude Sonnet 5.5", 2.00, 0.10, 10.00),
      new ModelPrice("Claude Haiku 5.5", 0.10, 0.01, 0.50),
      new ModelPrice("gemini-3.5-flash", 1.50, 0.15, 9.00),
      new ModelPrice("gemini-3.5-flash-lite", 0.30, 0.03, 2.50));

  public static void main(String[] args) {
    // 1. Count the tokens of a prompt before sending it
    Encoding encoding = Encodings.newDefaultEncodingRegistry().getEncoding(EncodingType.O200K_BASE);
    String question = "My order 1042 arrived damaged. Can I get a replacement or a refund?";
    int questionTokens = encoding.countTokens(question);
    System.out.println("Question tokens: " + questionTokens);

    // 2. One support-bot request: 5,000-token system prompt (long enough for every provider's cache minimum),
    //    1,000 tokens of history and question, 400-token answer
    long input = 6_000, cached = 5_000, output = 400, requestsPerMonth = 100_000;

    System.out.printf("%-22s %12s %12s %14s %14s%n", "Model", "Per request", "Monthly", "With caching", "Batch API");
    for (ModelPrice p : PRICES) {
      double perRequest = p.cost(input, 0, output);
      double monthly = perRequest * requestsPerMonth;
      double withCache = p.cost(input, cached, output) * requestsPerMonth;
      double batch = monthly * 0.5;
      System.out.printf("%-22s %12s %12s %14s %14s%n", p.model(),
          String.format("$%.5f", perRequest), String.format("$%,.0f", monthly),
          String.format("$%,.0f", withCache), String.format("$%,.0f", batch));
    }
  }
}
