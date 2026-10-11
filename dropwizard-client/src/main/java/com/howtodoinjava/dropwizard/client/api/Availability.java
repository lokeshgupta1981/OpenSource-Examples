package com.howtodoinjava.dropwizard.client.api;

public record Availability(String sku, int quantity, boolean available, String source) {
}
