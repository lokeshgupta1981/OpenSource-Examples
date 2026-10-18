package com.howtodoinjava.recipebox;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class RecipeBoxAssistant {

    // Any OpenAI-compatible server: Ollama, OpenAI, Azure OpenAI, vLLM
    static final String BASE_URL = env("LLM_BASE_URL", "http://localhost:11434/v1");
    static final String MODEL = env("LLM_MODEL", "qwen2.5:0.5b");
    static final String API_KEY = env("LLM_API_KEY", "");

    // The instructions: role, output format and one rule
    static final String SYSTEM_PROMPT = """
            You are the Recipe Box assistant. Answer only cooking questions.
            Reply in exactly this format and nothing else:
            Dish: <name>
            Time: <minutes> min
            Steps: <at most three short steps, separated by semicolons>
            If the question is not about cooking, reply exactly:
            Sorry, Recipe Box answers cooking questions only.""";

    // Few-shot examples: one question and the answer we want for it
    static final List<Map<String, String>> FEW_SHOT = List.of(
            message("user", "I have rice and peas. What can I cook?"),
            message("assistant", """
                    Dish: Pea pulao
                    Time: 25 min
                    Steps: Fry onion; add rice, peas and water; cook covered"""),
            message("user", "Who won the cricket match yesterday?"),
            message("assistant", "Sorry, Recipe Box answers cooking questions only."));

    static final HttpClient HTTP = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    static final JsonMapper JSON = JsonMapper.builder().build();

    public static void main(String[] args) throws IOException, InterruptedException {
        String question = "I have eggs, tomatoes and an onion. What can I cook?";

        // 1. The question alone
        String plain = chat(List.of(message("user", question)));
        System.out.println("--- Plain prompt ---\n" + plain);

        // 2. System prompt + few-shot examples + the same question
        String engineered = chat(engineered(question));
        System.out.println("\n--- Engineered prompt ---\n" + engineered);

        // 3. The rule in the system prompt, tested with an off-topic question
        String offTopic = chat(engineered("Write a poem about Mondays."));
        System.out.println("\n--- Off-topic question ---\n" + offTopic);
    }

    static List<Map<String, String>> engineered(String question) {
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(message("system", SYSTEM_PROMPT));
        messages.addAll(FEW_SHOT);
        messages.add(message("user", question));
        return messages;
    }

    static String chat(List<Map<String, String>> messages)
            throws IOException, InterruptedException {
        String body = JSON.writeValueAsString(Map.of(
                "model", MODEL,
                "messages", messages,
                "temperature", 0,
                "seed", 42));

        HttpRequest.Builder request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/chat/completions"))
                .timeout(Duration.ofSeconds(120))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body));
        if (!API_KEY.isBlank()) {
            request.header("Authorization", "Bearer " + API_KEY);
        }

        HttpResponse<String> response =
                HTTP.send(request.build(), HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new IOException("HTTP " + response.statusCode()
                    + ": " + response.body());
        }
        JsonNode root = JSON.readTree(response.body());
        return root.path("choices").path(0).path("message").path("content").asText();
    }

    static Map<String, String> message(String role, String content) {
        return Map.of("role", role, "content", content);
    }

    static String env(String name, String defaultValue) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? defaultValue : value;
    }
}
