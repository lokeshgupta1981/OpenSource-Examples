Source code for the article [Kafka MockConsumer and MockProducer Example (JUnit 6)](https://howtodoinjava.com/?p=38704)

A book order service: `BookOrderPublisher` sends `BookOrderEvent` records to Kafka, and
`BookOrderAggregator` reads them back and adds up the quantity per ISBN. The tests replace the real
Kafka clients with `MockProducer` and `MockConsumer`, so no broker is needed.

## Versions

- Java 17 or newer (JUnit 6 needs Java 17)
- Apache Kafka clients 4.4.0 (`kafka-clients`)
- Jackson Databind 2.20.1
- JUnit 6.1.3

## Run the tests

```bash
mvn test
```

All 5 tests pass:

- `BookOrderPublisherTest`: a successful send, and a send that fails with `errorNext()`.
- `BookOrderAggregatorTest`: adding up two orders, the `addRecord()`-before-`assign()` trap, and a
  poll failure with `setPollException()`.
