Source code for the article https://howtodoinjava.com/dropwizard/health-check-configuration-example/

An order service with two Dropwizard health checks (an inventory database and a payment gateway), the admin /healthcheck endpoint, the scheduled /health-check endpoint of the health module with alive and ready checks, a custom /status resource and an admin task that simulates a database outage.

## Versions

- Java 25 (Dropwizard 5 needs Java 17 or later)
- Dropwizard 5.0.2 (Jetty 12.1, Jersey 3.1, Metrics 4.2)
- JUnit 6.1.3 with dropwizard-testing
- Maven Shade Plugin 3.6.2

## Build and run

```bash
mvn package
java -jar target/dropwizard-health-check-1.0.0.jar server config.yml
```

```bash
curl http://localhost:8081/healthcheck?pretty=true
curl http://localhost:8080/health-check?name=all
curl http://localhost:8080/status
curl -X POST http://localhost:8081/tasks/database-outage
curl -X POST "http://localhost:8081/tasks/database-outage?down=false"
```

The file `k8s/probes.yml` shows the liveness and readiness probes for a Kubernetes Deployment.
