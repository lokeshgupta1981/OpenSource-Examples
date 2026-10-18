package com.howtodoinjava.kafkamock.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookOrderAggregatorTest {

    private static final String TOPIC = "book-order-events";
    private static final TopicPartition PARTITION = new TopicPartition(TOPIC, 0);

    private MockConsumer<String, String> consumer;
    private BookOrderAggregator aggregator;

    @BeforeEach
    void setUp() {
        consumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
        aggregator = new BookOrderAggregator(consumer);
    }

    @Test
    void consumeOnceAddsUpQuantitiesPerIsbn() throws Exception {
        consumer.assign(List.of(PARTITION));
        consumer.updateBeginningOffsets(Map.of(PARTITION, 0L));

        consumer.addRecord(new ConsumerRecord<>(TOPIC, 0, 0L, "ORD-2001",
                "{\"orderId\":\"ORD-2001\",\"isbn\":\"978-0134685991\",\"quantity\":3}"));
        consumer.addRecord(new ConsumerRecord<>(TOPIC, 0, 1L, "ORD-2002",
                "{\"orderId\":\"ORD-2002\",\"isbn\":\"978-0134685991\",\"quantity\":1}"));

        Map<String, Integer> totals = aggregator.consumeOnce();

        assertEquals(Map.of("978-0134685991", 4), totals);
    }

    @Test
    void addRecordBeforeAssignThrows() {
        ConsumerRecord<String, String> record =
                new ConsumerRecord<>(TOPIC, 0, 0L, "ORD-2003", "{}");

        IllegalStateException thrown = assertThrows(IllegalStateException.class,
                () -> consumer.addRecord(record));

        assertEquals("Cannot add records for a partition that is not assigned to the consumer",
                thrown.getMessage());
    }

    @Test
    void consumeOncePassesOnAPollFailure() {
        consumer.assign(List.of(PARTITION));
        consumer.updateBeginningOffsets(Map.of(PARTITION, 0L));
        consumer.setPollException(new KafkaException("broker unavailable"));

        KafkaException thrown = assertThrows(KafkaException.class, aggregator::consumeOnce);

        assertEquals("broker unavailable", thrown.getMessage());
    }
}
