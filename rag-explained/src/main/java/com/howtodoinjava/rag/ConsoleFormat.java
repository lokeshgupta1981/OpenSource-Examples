package com.howtodoinjava.rag;

/**
 * Console helpers for the example output; not part of the RAG mechanism.
 */
public final class ConsoleFormat {

    private ConsoleFormat() {
    }

    /** One line per chunk in the console: newlines removed, cut at 60 characters. */
    public static String shorten(String text) {
        String line = text.replace("\n", " | ");
        return line.length() > 60 ? line.substring(0, 57) + "..." : line;
    }

    /** Breaks a long answer into lines of at most 76 characters for the console. */
    public static String wrap(String text) {
        StringBuilder out = new StringBuilder("  ");
        int lineLength = 2;
        for (String word : text.split("\\s+")) {
            if (lineLength + word.length() > 76) {
                out.setLength(out.length() - 1);
                out.append("\n  ");
                lineLength = 2;
            }
            out.append(word).append(' ');
            lineLength += word.length() + 1;
        }
        return out.toString().stripTrailing();
    }
}
