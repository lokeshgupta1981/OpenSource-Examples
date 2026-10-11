package com.howtodoinjava.dropwizard.client.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.howtodoinjava.dropwizard.client.api.StockLevel;
import jakarta.ws.rs.core.UriBuilder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.HttpHeaders;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public class InventoryHttpClient {

    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper;
    private final String inventoryUrl;

    public InventoryHttpClient(CloseableHttpClient httpClient, ObjectMapper mapper, String inventoryUrl) {
        this.httpClient = httpClient;
        this.mapper = mapper;
        this.inventoryUrl = inventoryUrl;
    }

    public Optional<StockLevel> findStock(String sku) {
        URI uri = UriBuilder.fromUri(inventoryUrl).path("inventory/{sku}").build(sku);
        HttpGet request = new HttpGet(uri);
        request.addHeader(HttpHeaders.ACCEPT, "application/json");
        try {
            return httpClient.execute(request, response -> {
                int status = response.getCode();
                if (status == 404) {
                    return Optional.empty();
                }
                if (status != 200) {
                    throw new InventoryUnavailableException("Inventory returned " + status);
                }
                return Optional.of(mapper.readValue(response.getEntity().getContent(), StockLevel.class));
            });
        } catch (IOException e) {
            throw new InventoryUnavailableException("Inventory call failed: " + e.getMessage(), e);
        }
    }
}
