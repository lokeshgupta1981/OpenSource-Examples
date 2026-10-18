package com.howtodoinjava.rag;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Sends one prompt to a local Ollama server and returns the model's text answer.
 * This is the "generate" phase of RAG; the class knows nothing about retrieval.
 */
public class OllamaClient {

    /** The same instruction for every call, with or without RAG. */
    static final String SYSTEM_MESSAGE =
            "You are a cooking assistant. Answer in one or two sentences.";

    private static final ObjectMapper JSON = new ObjectMapper();

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final String baseUrl;
    private final String model;

    public OllamaClient(String baseUrl, String model) {
        this.baseUrl = baseUrl;
        this.model = model;
    }

    /** Reads OLLAMA_URL and OLLAMA_MODEL, with the Ollama defaults as fallback. */
    public static OllamaClient fromEnvironment() {
        String url = System.getenv()
                .getOrDefault("OLLAMA_URL", "http://localhost:11434");
        String model = System.getenv().getOrDefault("OLLAMA_MODEL", "qwen2.5:0.5b");
        return new OllamaClient(url, model);
    }

    public String generate(String prompt) throws IOException, InterruptedException {
        Map<String, Object> body = Map.of(
                "model", model,
                "stream", false,
                "messages", List.of(
                        Map.of("role", "system", "content", SYSTEM_MESSAGE),
                        Map.of("role", "user", "content", prompt)),
                // temperature 0 and a fixed seed give the same answer on every run;
                // num_predict caps the answer length in tokens
                "options", Map.of("temperature", 0, "seed", 42, "num_predict", 200));

        HttpRequest request = HttpRequest.newBuilder(URI.create(baseUrl + "/api/chat"))
                .timeout(Duration.ofMinutes(2))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JSON.writeValueAsString(body)))
                .build();

        HttpResponse<String> response =
                http.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("Ollama returned " + response.statusCode()
                    + ": " + response.body());
        }
        JsonNode json = JSON.readTree(response.body());
        return json.path("message").path("content").asText().strip();
    }
}
