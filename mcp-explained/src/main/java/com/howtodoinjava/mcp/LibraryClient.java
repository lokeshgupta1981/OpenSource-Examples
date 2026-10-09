package com.howtodoinjava.mcp;

import io.modelcontextprotocol.client.McpClient;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.client.transport.ServerParameters;
import io.modelcontextprotocol.client.transport.StdioClientTransport;
import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.spec.McpSchema;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Map;

public class LibraryClient {

  public static void main(String[] args) {
    // 1. Start the server as a child process and talk to it over stdio
    ServerParameters params = ServerParameters.builder(Path.of(System.getProperty("java.home"), "bin", "java").toString())
        .args("-cp", System.getProperty("java.class.path"), "com.howtodoinjava.mcp.LibraryServer")
        .build();

    try (McpSyncClient client = McpClient.sync(new StdioClientTransport(params, McpJsonDefaults.getMapper()))
        .requestTimeout(Duration.ofSeconds(10))
        .build()) {

      // 2. Handshake, then discover the tools
      McpSchema.InitializeResult init = client.initialize();
      System.out.println("Connected to " + init.serverInfo().name() + ", protocol " + init.protocolVersion());

      McpSchema.ListToolsResult tools = client.listTools();
      tools.tools().forEach(t -> System.out.println("Tool: " + t.name() + " - " + t.description()));

      // 3. Call the tool twice, once with an unknown title
      for (String title : new String[] {"Clean Code", "Dune"}) {
        McpSchema.CallToolResult result = client.callTool(
            McpSchema.CallToolRequest.builder("check_availability").arguments(Map.of("title", title)).build());
        String text = ((McpSchema.TextContent) result.content().get(0)).text();
        System.out.println(title + " -> isError=" + result.isError() + ", " + text);
      }
    }
  }
}
