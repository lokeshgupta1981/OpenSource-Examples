Source code for the article https://howtodoinjava.com/ai/model-context-protocol-mcp-java/

A minimal MCP server with one tool (check_availability) and a client that starts it over stdio, written with the MCP Java SDK. The spring-ai-server folder has the same tool as a Spring AI @McpTool bean.

Versions: Java 25, Maven 3.9, MCP Java SDK 2.0.1, Spring Boot 4.1.1, Spring AI 2.0.1

Run the SDK client (it starts the server as a child process):

    mvn compile dependency:build-classpath -Dmdep.outputFile=cp.txt
    java -cp "target/classes:$(cat cp.txt)" com.howtodoinjava.mcp.LibraryClient

Run the Spring AI server and send JSON-RPC messages on standard input:

    cd spring-ai-server
    mvn package
    java -jar target/mcp-spring-ai-server-1.0.jar
