package com.howtodoinjava.kafkamock.model;

public record BookOrderEvent(String orderId, String isbn, int quantity) {
}
