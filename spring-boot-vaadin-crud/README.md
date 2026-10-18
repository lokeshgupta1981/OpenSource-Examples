Source code for the article [Spring Boot and Vaadin CRUD Example](https://howtodoinjava.com/?p=46848)

Versions used: Java 21, Spring Boot 4.1.1, Vaadin 25.3.2, H2 (in-memory).

## Run it

```
mvn spring-boot:run
```

This starts the embedded dev server on the first run, which needs Node.js and takes a minute to
prepare the frontend bundle. Open http://localhost:8080 once the log prints "Started
VaadinCrudApplication".

To run the same jar the way it would run in a real deployment, build the production bundle first:

```
mvn -Pproduction clean package
java -jar target/spring-boot-vaadin-crud-1.0.0.jar
```

## What it is

A one-table CRUD app: a *Book* entity (title, author, available) backed by Spring Data JPA and H2,
with a Vaadin *Grid* and a *Binder*-backed form for create, update and delete. Three books are
seeded through *data.sql*.

## Tests

```
mvn test
```

*BookRepositoryTest* saves a book and reads it back by title, and checks the seed data loaded.
Both run against the real H2 database, not a mock.
