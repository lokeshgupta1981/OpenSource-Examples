package com.howtodoinjava.dropwizard.health.health;

import com.codahale.metrics.health.HealthCheck;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class PaymentGatewayHealthCheck extends HealthCheck {

    private final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(2))
            .build();
    private final URI statusUrl;

    public PaymentGatewayHealthCheck(URI statusUrl) {
        this.statusUrl = statusUrl;
    }

    @Override
    protected Result check() throws InterruptedException {
        HttpRequest request = HttpRequest.newBuilder(statusUrl)
                .timeout(Duration.ofSeconds(2))
                .GET()
                .build();
        long start = System.nanoTime();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            long millis = Duration.ofNanos(System.nanoTime() - start).toMillis();

            if (response.statusCode() != 200) {
                return Result.unhealthy("Payment gateway returned HTTP %d", response.statusCode());
            }
            return Result.builder()
                    .healthy()
                    .withMessage("Payment gateway is up")
                    .withDetail("responseTimeMs", millis)
                    .build();
        } catch (IOException e) {
            return Result.unhealthy("Payment gateway unreachable: %s", e.getClass().getSimpleName());
        }
    }
}
