package com.howtodoinjava.dropwizard.configerror;

import io.dropwizard.core.Configuration;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class NotesApplicationTest {

    static final DropwizardAppExtension<Configuration> APP = new DropwizardAppExtension<>(
            NotesApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

    @Test
    void startsWithHttpConnector() {
        int status = APP.client()
                .target("http://localhost:%d/notes".formatted(APP.getLocalPort()))
                .request()
                .get()
                .getStatus();

        assertEquals(200, status);
    }
}
