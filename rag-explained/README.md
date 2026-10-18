Source code for the article https://howtodoinjava.com/?p=44492

A minimal retrieval augmented generation (RAG) pipeline in plain Java for a private Recipe Box:
four recipe text files are split into chunks, embedded with the offline all-MiniLM-L6-v2 model
(LangChain4j, runs inside the JVM, no API key), kept in a hand-written in-memory vector store,
searched with cosine similarity, and the top-k chunks go into the prompt of a local Ollama model.

Versions: Java 17 or newer (run on Java 25), Maven 3.9, LangChain4j
langchain4j-embeddings-all-minilm-l6-v2 1.22.0-beta32, Ollama with qwen2.5:0.5b.

Classes:

- AskWithoutRag: asks the model about the private recipe with no retrieved text (wrong answer).
- RecipeBoxRag: index, retrieve, generate; prints the top-k chunks, the prompt and the answer.
- RagTrap: the wrong-chunk trap (chunks without the recipe title) and the loose-prompt trap.
- InMemoryVectorStore: a list of chunk + vector pairs with a cosine similarity search.
- OllamaClient: one HTTP call to Ollama's /api/chat endpoint.
- ConsoleFormat: console helpers that shorten chunks and wrap answers in the output.

Run:

    ollama pull qwen2.5:0.5b
    mvn compile exec:java
    mvn compile exec:java -Dexec.mainClass=com.howtodoinjava.rag.AskWithoutRag
    mvn compile exec:java -Dexec.mainClass=com.howtodoinjava.rag.RagTrap

Ollama is expected at http://localhost:11434. Set OLLAMA_URL to use another address and
OLLAMA_MODEL to use another model. Without a running Ollama server, RecipeBoxRag still indexes,
retrieves and prints the prompt, and says that no server was found instead of the answer.
