Source code for the article [Run LLMs Locally for Java: Ollama vs Docker Model Runner vs LM Studio](https://howtodoinjava.com/ai/run-llms-locally/)

A Recipe Box question sent from plain Java (JDK `java.net.http.HttpClient`, no framework) to a
local model server's OpenAI-compatible `/v1/chat/completions` endpoint. The same class works with
Ollama, Docker Model Runner and LM Studio; only the base URL and the model name change.

## Versions

- Java 17 or newer (built and run on OpenJDK 25.0.4.1)
- Maven 3.9.11
- Jackson 2.22.4 (`com.fasterxml.jackson.core:jackson-databind`)
- Tested against Ollama 0.40.6 with the model `qwen2.5:0.5b`

## Configuration

The class reads two settings, first from the program arguments, then from environment variables,
then from the defaults.

| Setting | Argument | Environment variable | Default |
|---|---|---|---|
| Base URL | 1st | `LLM_BASE_URL` | `http://localhost:11434/v1` |
| Model name | 2nd | `LLM_MODEL` | `qwen2.5:0.5b` |

## Run against Ollama

```bash
ollama pull qwen2.5:0.5b
mvn compile exec:java
```

## Run against Docker Model Runner

Enable Model Runner with host-side TCP support on port 12434 (Docker Desktop: Settings > AI), then:

```bash
docker model pull ai/smollm2
mvn compile exec:java -Dexec.args="http://localhost:12434/engines/v1 ai/smollm2"
```

## Run against LM Studio

Download a model in LM Studio (a 4-bit option), load it, and start the server (Developer tab >
Start server, or `lms load` and `lms server start --port 1234`). Read the model id from
`curl -s http://localhost:1234/v1/models` and pass it in place of MODEL_ID:

```bash
mvn compile exec:java -Dexec.args="http://localhost:1234/v1 MODEL_ID"
```

## Expected output (Ollama)

```text
Runner: http://localhost:11434/v1 (model qwen2.5:0.5b)
Question: What can I cook tonight with rice, eggs and spring onions?
Answer: You can cook rice, eggs, and spring onions with a simple stir-fry.
Took: 807 ms
```
