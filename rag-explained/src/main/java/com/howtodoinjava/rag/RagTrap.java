package com.howtodoinjava.rag;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

/**
 * The two ways a RAG answer goes wrong: the wrong chunks come back from the search,
 * or the model answers from its own memory instead of the retrieved text.
 */
public class RagTrap {

    public static void main(String[] args) throws Exception {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();

        // Trap 1: chunks without the recipe title, and only the top-1 chunk is used.
        String bakeQuestion =
                "How long do I bake the lemon cake, and at what temperature?";
        System.out.println("Trap 1, question: " + bakeQuestion);
        System.out.println("Chunks WITHOUT the title:");
        answerFromTop1(embeddingModel, chunkWithoutTitle(), bakeQuestion);
        System.out.println("Chunks WITH the title:");
        answerFromTop1(embeddingModel, chunkWithTitle(), bakeQuestion);

        // Trap 2: the answer is not in the Recipe Box at all.
        String freezeQuestion = "Can I freeze Nani's Lemon Semolina Cake?";
        InMemoryVectorStore store = new InMemoryVectorStore();
        for (String chunk : chunkWithTitle()) {
            store.add(chunk, embeddingModel.embed(chunk).content().vector());
        }
        float[] questionVector = embeddingModel.embed(freezeQuestion).content().vector();
        List<InMemoryVectorStore.Match> matches =
                store.search(questionVector, RecipeBoxRag.TOP_K);

        System.out.println();
        System.out.println("Trap 2, question: " + freezeQuestion);
        String looseAnswer =
                RecipeBoxRag.generateOrExplain(loosePrompt(freezeQuestion, matches));
        String strictAnswer = RecipeBoxRag.generateOrExplain(
                RecipeBoxRag.buildPrompt(freezeQuestion, matches));
        System.out.println("Loose prompt answer:");
        System.out.println(ConsoleFormat.wrap(looseAnswer));
        System.out.println("Strict prompt answer:");
        System.out.println(ConsoleFormat.wrap(strictAnswer));
    }

    /** The broken prompt: it lets the model add what it "knows" to the context. */
    static String loosePrompt(String question, List<InMemoryVectorStore.Match> matches) {
        List<String> chunks = matches.stream()
                .map(InMemoryVectorStore.Match::chunk)
                .toList();
        return """
                Use the context below to help you answer.

                Question: %s

                Context:
                %s""".formatted(question, String.join("\n\n", chunks));
    }

    static List<String> chunkWithTitle() {
        List<String> chunks = new ArrayList<>();
        for (String file : RecipeBoxRag.RECIPE_FILES) {
            chunks.addAll(RecipeBoxRag.chunk(RecipeBoxRag.loadRecipe(file)));
        }
        return chunks;
    }

    /** The broken chunker: one chunk per paragraph, and the title is dropped. */
    static List<String> chunkWithoutTitle() {
        List<String> chunks = new ArrayList<>();
        for (String file : RecipeBoxRag.RECIPE_FILES) {
            String recipe = RecipeBoxRag.loadRecipe(file);
            String[] paragraphs = recipe.strip().split("\\n\\s*\\n");
            for (int i = 1; i < paragraphs.length; i++) {
                chunks.add(paragraphs[i].strip());
            }
        }
        return chunks;
    }

    static void answerFromTop1(EmbeddingModel model, List<String> chunks,
            String question) throws InterruptedException {
        InMemoryVectorStore store = new InMemoryVectorStore();
        for (String chunk : chunks) {
            store.add(chunk, model.embed(chunk).content().vector());
        }
        float[] questionVector = model.embed(question).content().vector();
        List<InMemoryVectorStore.Match> top1 = store.search(questionVector, 1);

        System.out.printf(Locale.ROOT, "  %.3f  %s%n", top1.get(0).score(),
                ConsoleFormat.shorten(top1.get(0).chunk()));
        String prompt = RecipeBoxRag.buildPrompt(question, top1);
        String answer = RecipeBoxRag.generateOrExplain(prompt);
        System.out.println(ConsoleFormat.wrap("Answer: " + answer));
    }
}
