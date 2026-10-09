package com.howtodoinjava.mcp;

import io.modelcontextprotocol.json.McpJsonDefaults;
import io.modelcontextprotocol.server.McpServer;
import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.transport.StdioServerTransportProvider;
import io.modelcontextprotocol.spec.McpSchema;
import java.util.List;
import java.util.Map;

public class LibraryServer {

  public static void main(String[] args) {
    Map<String, Integer> copies = Map.of("clean code", 2, "effective java", 0, "refactoring", 1);

    // 1. The tool's input: one required string argument named "title"
    Map<String, Object> inputSchema = Map.of(
        "type", "object",
        "properties", Map.of("title", Map.of("type", "string", "description", "Book title")),
        "required", List.of("title"));

    // 2. The tool definition plus the Java code that runs when a client calls it
    McpServerFeatures.SyncToolSpecification checkAvailability =
        McpServerFeatures.SyncToolSpecification.builder()
            .tool(McpSchema.Tool.builder("check_availability", inputSchema)
                .description("Returns how many copies of a book the library has on the shelf")
                .build())
            .callHandler((exchange, request) -> {
              String title = String.valueOf(request.arguments().get("title")).toLowerCase();
              Integer count = copies.get(title);
              if (count == null) {
                return McpSchema.CallToolResult.builder()
                    .addTextContent("No book with the title '" + title + "' in the catalog")
                    .isError(true)
                    .build();
              }
              return McpSchema.CallToolResult.builder()
                  .addTextContent(count + " copies of '" + title + "' available")
                  .build();
            })
            .build();

    // 3. A server that talks JSON-RPC over stdin and stdout
    McpSyncServer server = McpServer.sync(new StdioServerTransportProvider(McpJsonDefaults.getMapper()))
        .serverInfo("library-server", "1.0.0")
        .capabilities(McpSchema.ServerCapabilities.builder().tools(true).build())
        .tools(checkAvailability)
        .build();
  }
}
