package com.howtodoinjava.tokens;

// Prices in US dollars per 1 million tokens, taken from the providers' pricing pages on 2026-10-09
public record ModelPrice(String model, double input, double cachedInput, double output) {

  public double cost(long inputTokens, long cachedTokens, long outputTokens) {
    long uncached = inputTokens - cachedTokens;
    return (uncached * input + cachedTokens * cachedInput + outputTokens * output) / 1_000_000;
  }
}
