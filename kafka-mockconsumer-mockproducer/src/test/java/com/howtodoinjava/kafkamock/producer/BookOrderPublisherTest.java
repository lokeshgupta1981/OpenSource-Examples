package com.howtodoinjava.kafkamock.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.howtodoinjava.kafkamock.model.BookOrderEvent;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.errors.TimeoutException;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookOrderPublisherTest {

    @Test
    void publishSendsTheOrderAsJson() throws Exception {
        // autoComplete = true: every send() succeeds immediately
        MockProducer<String, String> producer =
                new MockProducer<>(true, null, new StringSerializer(), new StringSerializer());
        BookOrderPublisher publisher = new BookOrderPublisher(producer);

        BookOrderEvent order = new BookOrderEvent("ORD-1001", "978-0134685991", 2);
        RecordMetadata metadata = publisher.publish(order).get();

        assertEquals("book-order-events", metadata.topic());
        assertEquals(1, producer.history().size());

        ProducerRecord<String, String> sent = producer.history().get(0);
        assertEquals("ORD-1001", sent.key());
        String expectedJson =
                "{\"orderId\":\"ORD-1001\",\"isbn\":\"978-0134685991\",\"quantity\":2}";
        assertEquals(expectedJson, sent.value());
    }

    @Test
    void publishReportsABrokerFailure() throws JsonProcessingException {
        // autoComplete = false: send() waits for completeNext() or errorNext()
        MockProducer<String, String> producer =
                new MockProducer<>(false, null, new StringSerializer(), new StringSerializer());
        BookOrderPublisher publisher = new BookOrderPublisher(producer);

        BookOrderEvent order = new BookOrderEvent("ORD-1002", "978-0321356680", 1);
        Future<RecordMetadata> result = publisher.publish(order);
        assertFalse(result.isDone());

        producer.errorNext(new TimeoutException("broker did not respond"));

        ExecutionException thrown = assertThrows(ExecutionException.class, result::get);
        assertInstanceOf(TimeoutException.class, thrown.getCause());
    }
}
