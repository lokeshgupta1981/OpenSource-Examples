package com.howtodoinjava.dropwizard.helloworld;

import com.howtodoinjava.dropwizard.helloworld.api.Greeting;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class TaskApplicationTest {

    static final DropwizardAppExtension<TaskConfiguration> APP = new DropwizardAppExtension<>(
            TaskApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

    @Test
    void greetsByName() {
        Greeting greeting = APP.client()
                .target("http://localhost:%d/hello".formatted(APP.getLocalPort()))
                .queryParam("name", "Lokesh")
                .request()
                .get(Greeting.class);

        assertEquals("Hello, Lokesh!", greeting.content());
    }

    @Test
    void healthCheckIsHealthy() {
        Response response = APP.client()
                .target("http://localhost:%d/healthcheck".formatted(APP.getAdminPort()))
                .request()
                .get();

        assertEquals(200, response.getStatus());
    }
}
