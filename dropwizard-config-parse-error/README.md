Source code for the article https://howtodoinjava.com/dropwizard/solved-dropwizard-failed-parse-configuration-not-resolve-type-id-http-subtype/

Reproduces and fixes the Dropwizard startup error "Failed to parse configuration at: server.applicationConnectors.[0]; Could not resolve type id 'http' as a subtype of io.dropwizard.jetty.ConnectorFactory".

## Versions

- Java 25 (Dropwizard 5 needs Java 17 or later)
- Dropwizard 5.0.2
- Maven Shade Plugin 3.6.2
- JUnit 6.1.3

## Reproduce the error

`pom-broken.xml` builds the fat jar without the `ServicesResourceTransformer`.

```bash
mvn -f pom-broken.xml package -DskipTests
java -jar target/dropwizard-config-parse-error-broken-1.0.0.jar check config.yml
```

## Fixed build

```bash
mvn package
java -jar target/dropwizard-config-parse-error-1.0.0.jar check config.yml
java -jar target/dropwizard-config-parse-error-1.0.0.jar server config.yml
curl http://localhost:8080/notes
```

## Other causes

- `broken-configs/typo-in-type.yml` uses an unknown connector type.
- `broken-configs/h2c-without-module.yml` uses the `h2c` connector, which needs `dropwizard-http2`. Build with `mvn package -Phttp2` to add it.
