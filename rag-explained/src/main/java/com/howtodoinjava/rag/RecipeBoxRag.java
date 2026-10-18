package com.howtodoinjava.rag;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.ConnectException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;

/**
 * A minimal RAG pipeline for a private Recipe Box: index, retrieve, generate.
 */
public class RecipeBoxRag {

    static final List<String> RECIPE_FILES = List.of(
            "lemon-semolina-cake.txt",
            "banana-walnut-bread.txt",
            "tomato-rasam.txt",
            "sunday-pancakes.txt");

    static final int TOP_K = 2;

    public static void main(String[] args) throws Exception {
        String question = args.length > 0
                ? String.join(" ", args)
                : "How long do I bake Nani's Lemon Semolina Cake, "
                        + "and at what temperature?";

        // embedding model: runs inside the JVM, no API key, 384 numbers per text
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        InMemoryVectorStore store = new InMemoryVectorStore();

        // Phase 1: index. Chunk every document, embed every chunk, store both.
        for (String file : RECIPE_FILES) {
            for (String chunk : chunk(loadRecipe(file))) {
                float[] vector = embeddingModel.embed(chunk).content().vector();
                store.add(chunk, vector);
            }
        }
        System.out.println("Indexed " + store.size() + " chunks");

        // Phase 2: retrieve. Embed the question, then run a similarity search.
        float[] questionVector = embeddingModel.embed(question).content().vector();
        List<InMemoryVectorStore.Match> matches = store.search(questionVector, TOP_K);

        System.out.println("Question: " + question);
        System.out.println("Top-" + TOP_K + " chunks:");
        for (InMemoryVectorStore.Match match : matches) {
            System.out.printf(Locale.ROOT, "  %.3f  %s%n", match.score(),
                    ConsoleFormat.shorten(match.chunk()));
        }

        // Phase 3: generate. Put the retrieved text into the prompt, then call the LLM.
        String prompt = buildPrompt(question, matches);
        System.out.println();
        System.out.println("Prompt sent to the model:");
        System.out.println(prompt);
        System.out.println();
        System.out.println("Answer:");
        System.out.println(ConsoleFormat.wrap(generateOrExplain(prompt)));
    }

    /** Splits a recipe into chunks: one per paragraph, each starting with the title. */
    static List<String> chunk(String recipe) {
        String[] paragraphs = recipe.strip().split("\\n\\s*\\n");
        String title = paragraphs[0].strip();
        List<String> chunks = new ArrayList<>();
        for (int i = 1; i < paragraphs.length; i++) {
            chunks.add(title + "\n" + paragraphs[i].strip());
        }
        return chunks;
    }

    /** The augmented prompt: instruction, question, then the retrieved chunks. */
    static String buildPrompt(String question, List<InMemoryVectorStore.Match> matches) {
        List<String> chunks = matches.stream()
                .map(InMemoryVectorStore.Match::chunk)
                .toList();
        return """
                Answer the question using only the context below.
                If the context does not contain the answer,
                say "The Recipe Box does not say."

                Question: %s

                Context:
                %s""".formatted(question, String.join("\n\n", chunks));
    }

    static String generateOrExplain(String prompt) throws InterruptedException {
        try {
            return OllamaClient.fromEnvironment().generate(prompt);
        } catch (ConnectException e) {
            return "(no Ollama server found; start one to see the generated answer)";
        } catch (IOException e) {
            return "(Ollama call failed: " + e.getMessage() + ")";
        }
    }

    static String loadRecipe(String file) {
        String path = "/recipes/" + file;
        try (InputStream in = RecipeBoxRag.class.getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalStateException("Recipe not found: " + file);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
