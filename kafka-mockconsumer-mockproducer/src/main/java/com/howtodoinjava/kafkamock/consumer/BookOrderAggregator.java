package com.howtodoinjava.kafkamock.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.howtodoinjava.kafkamock.model.BookOrderEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class BookOrderAggregator {

    private final Consumer<String, String> consumer;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Map<String, Integer> quantityByIsbn = new HashMap<>();

    // Same idea as the publisher: a test passes a MockConsumer here
    public BookOrderAggregator(Consumer<String, String> consumer) {
        this.consumer = consumer;
    }

    // Polls once and adds the quantity of every order to the running total for its ISBN
    public Map<String, Integer> consumeOnce() throws JsonProcessingException {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(500));
        for (ConsumerRecord<String, String> record : records) {
            BookOrderEvent event =
                    objectMapper.readValue(record.value(), BookOrderEvent.class);
            quantityByIsbn.merge(event.isbn(), event.quantity(), Integer::sum);
        }
        return quantityByIsbn;
    }
}
