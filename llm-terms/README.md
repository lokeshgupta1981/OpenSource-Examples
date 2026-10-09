Source code for the article https://howtodoinjava.com/ai/llm-terms-for-java-developers/

Small, runnable Java examples for three LLM terms: tokens (JTokkit), cosine similarity of embedding vectors, and how temperature changes next-token probabilities.

Versions: Java 25, Maven 3.9, JTokkit 1.1.0

Run:

    mvn compile
    mvn exec:java -Dexec.mainClass=com.howtodoinjava.llm.TokenCountExample
    mvn exec:java -Dexec.mainClass=com.howtodoinjava.llm.CosineSimilarityExample
    mvn exec:java -Dexec.mainClass=com.howtodoinjava.llm.TemperatureExample

The vectors and scores in CosineSimilarityExample and TemperatureExample are made-up example values.
