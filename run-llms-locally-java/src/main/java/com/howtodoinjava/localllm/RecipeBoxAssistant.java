package com.howtodoinjava.localllm;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecipeBoxAssistant {

    // Request body of POST /chat/completions in the OpenAI format
    record Message(String role, String content) {}

    record ChatRequest(String model, List<Message> messages, double temperature) {}

    private static final String DEFAULT_BASE_URL = "http://localhost:11434/v1";
    private static final String DEFAULT_MODEL = "qwen2.5:0.5b";
    private static final String SYSTEM_PROMPT =
            "You are a Recipe Box assistant. Answer in two sentences.";

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper json = new ObjectMapper();

    private final String baseUrl;
    private final String model;

    public RecipeBoxAssistant(String baseUrl, String model) {
        this.baseUrl = baseUrl;
        this.model = model;
    }

    public String ask(String question) throws IOException, InterruptedException {
        ChatRequest body = new ChatRequest(model, List.of(
                new Message("system", SYSTEM_PROMPT),
                new Message("user", question)), 0.0);

        String requestJson = json.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/chat/completions"))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(120))
                .POST(HttpRequest.BodyPublishers.ofString(requestJson))
                .build();

        HttpResponse<String> response =
                http.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IllegalStateException(
                    "HTTP " + response.statusCode() + ": " + response.body());
        }

        JsonNode root = json.readTree(response.body());
        return root.at("/choices/0/message/content").asText();
    }

    public static void main(String[] args) throws Exception {
        String baseUrl = setting(args, 0, "LLM_BASE_URL", DEFAULT_BASE_URL);
        String model = setting(args, 1, "LLM_MODEL", DEFAULT_MODEL);

        RecipeBoxAssistant assistant = new RecipeBoxAssistant(baseUrl, model);
        String question = "What can I cook tonight with rice, eggs and spring onions?";

        long start = System.currentTimeMillis();
        String answer = assistant.ask(question);
        long millis = System.currentTimeMillis() - start;

        System.out.println("Runner: " + baseUrl + " (model " + model + ")");
        System.out.println("Question: " + question);
        System.out.println("Answer: " + answer);
        System.out.println("Took: " + millis + " ms");
    }

    // Program argument first, then environment variable, then the default
    private static String setting(String[] args, int index,
                                  String env, String fallback) {
        if (args.length > index && !args[index].isBlank()) {
            return args[index];
        }
        String value = System.getenv(env);
        return (value == null || value.isBlank()) ? fallback : value;
    }
}
