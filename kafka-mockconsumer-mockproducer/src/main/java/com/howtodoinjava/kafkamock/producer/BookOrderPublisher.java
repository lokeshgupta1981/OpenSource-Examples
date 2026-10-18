package com.howtodoinjava.kafkamock.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.howtodoinjava.kafkamock.model.BookOrderEvent;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.concurrent.Future;

public class BookOrderPublisher {

    public static final String TOPIC = "book-order-events";

    private final Producer<String, String> producer;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // The publisher receives a Producer instead of creating a KafkaProducer itself,
    // so a test can pass a MockProducer here
    public BookOrderPublisher(Producer<String, String> producer) {
        this.producer = producer;
    }

    public Future<RecordMetadata> publish(BookOrderEvent event)
            throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(event);
        return producer.send(new ProducerRecord<>(TOPIC, event.orderId(), json));
    }
}
