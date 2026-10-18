package com.howtodoinjava.rag;

/**
 * Asks the model about a private recipe with no retrieved text in the prompt.
 * The model has never seen our Recipe Box, so it can only guess.
 */
public class AskWithoutRag {

    public static void main(String[] args) throws Exception {
        String question = "How long do I bake Nani's Lemon Semolina Cake, "
                + "and at what temperature?";

        OllamaClient llm = OllamaClient.fromEnvironment();
        String answer = llm.generate(question);

        System.out.println("Question: " + question);
        System.out.println("Answer:");
        System.out.println(answer);
    }
}
