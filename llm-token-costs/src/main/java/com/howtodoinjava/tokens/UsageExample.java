package com.howtodoinjava.tokens;

import java.util.List;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.metadata.ChatResponseMetadata;
import org.springframework.ai.chat.metadata.DefaultUsage;
import org.springframework.ai.chat.metadata.Usage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;

public class UsageExample {

  public static void main(String[] args) {
    // A stand-in ChatModel that returns fixed token usage, so the example runs without an API key.
    // In a real app, the provider starter (OpenAI, Anthropic, Gemini, Ollama ...) creates the ChatModel.
    ChatModel model = (Prompt prompt) -> new ChatResponse(
        List.of(new Generation(new AssistantMessage("You can get a replacement within 30 days."))),
        ChatResponseMetadata.builder().usage(new DefaultUsage(6000, 400, 6400, null, 5000L, 0L)).build());

    ChatClient chatClient = ChatClient.create(model);

    ChatResponse response = chatClient.prompt()
        .user("My order 1042 arrived damaged. Can I get a replacement?")
        .call()
        .chatResponse();

    Usage usage = response.getMetadata().getUsage();
    System.out.println("Prompt tokens: " + usage.getPromptTokens());
    System.out.println("Completion tokens: " + usage.getCompletionTokens());
    System.out.println("Total tokens: " + usage.getTotalTokens());
    System.out.println("Cache read tokens: " + usage.getCacheReadInputTokens());

    ModelPrice sonnet = new ModelPrice("Claude Sonnet 5.5", 2.00, 0.10, 10.00);
    double cost = sonnet.cost(usage.getPromptTokens(), usage.getCacheReadInputTokens(), usage.getCompletionTokens());
    System.out.printf("Cost of this call: $%.5f%n", cost);
  }
}
