package com.howtodoinjava.rag;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * A vector store kept in a plain list: every chunk is saved next to its embedding,
 * and a search compares the question's embedding with all of them.
 */
public class InMemoryVectorStore {

    public record Entry(String chunk, float[] embedding) {
    }

    public record Match(String chunk, double score) {
    }

    private final List<Entry> entries = new ArrayList<>();

    public void add(String chunk, float[] embedding) {
        entries.add(new Entry(chunk, embedding));
    }

    public int size() {
        return entries.size();
    }

    /** Similarity search: score every stored chunk and keep the top-k. */
    public List<Match> search(float[] questionEmbedding, int topK) {
        return entries.stream()
                .map(e -> new Match(e.chunk(), cosineSimilarity(questionEmbedding,
                        e.embedding())))
                .sorted(Comparator.comparingDouble(Match::score).reversed())
                .limit(topK)
                .toList();
    }

    /** Cosine similarity = (A . B) / (|A| * |B|), a number from -1 to 1. */
    static double cosineSimilarity(float[] a, float[] b) {
        double dot = 0;
        double normA = 0;
        double normB = 0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
            normA += a[i] * a[i];
            normB += b[i] * b[i];
        }
        return dot / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
