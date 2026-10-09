Source code for the article https://howtodoinjava.com/ai/llm-token-costs-java/

Estimates the cost of LLM requests from token counts and prices, counts tokens with JTokkit, and reads token usage from a Spring AI ChatResponse.

Versions: Java 25, Maven 3.9, JTokkit 1.1.0, Spring AI 2.0.1

Run:

    mvn compile
    mvn exec:java -Dexec.mainClass=com.howtodoinjava.tokens.CostEstimator
    mvn exec:java -Dexec.mainClass=com.howtodoinjava.tokens.UsageExample

Prices are from the OpenAI, Anthropic and Gemini pricing pages on October 9, 2026. UsageExample uses a stand-in ChatModel with fixed usage numbers, so it needs no API key.
