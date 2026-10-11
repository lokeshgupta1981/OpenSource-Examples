Source code for the article https://howtodoinjava.com/dropwizard/tutorial-and-hello-world-example/

A small task service built with Dropwizard: a hello world endpoint, a CRUD resource for tasks with Bean Validation, a health check and tests.

## Versions

- Java 25 (Dropwizard 5 needs Java 17 or later)
- Dropwizard 5.0.2 (Jetty 12.1, Jersey 3.1, Jackson 2.21, Hibernate Validator 8)
- JUnit 6.1.3 with dropwizard-testing
- Maven Shade Plugin 3.6.2

## Build and run

```bash
mvn package
java -jar target/dropwizard-hello-world-1.0.0.jar check config.yml
java -jar target/dropwizard-hello-world-1.0.0.jar server config.yml
```

The application listens on http://localhost:8080 and the admin interface on http://localhost:8081.

```bash
curl http://localhost:8080/hello?name=Lokesh
curl -X POST http://localhost:8080/tasks -H 'Content-Type: application/json' -d '{"title":"Buy milk"}'
curl http://localhost:8080/tasks
curl http://localhost:8081/healthcheck
```
