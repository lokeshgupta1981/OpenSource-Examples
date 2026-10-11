package com.howtodoinjava.dropwizard.auth;

import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class InvoiceApplicationTest {

    private static final DropwizardAppExtension<InvoiceConfiguration> APP = new DropwizardAppExtension<>(
            InvoiceApplication.class, ResourceHelpers.resourceFilePath("test-config.yml"));

    private static String basic(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    private Response call(String method, String path, String authorization) {
        var request = APP.client()
                .target("http://localhost:" + APP.getLocalPort() + path)
                .request();
        if (authorization != null) {
            request = request.header(HttpHeaders.AUTHORIZATION, authorization);
        }
        return request.method(method);
    }

    @Test
    void missingCredentialsGive401WithChallenge() {
        Response response = call("GET", "/invoices", null);
        assertEquals(401, response.getStatus());
        assertEquals("Basic realm=\"invoices\"", response.getHeaderString(HttpHeaders.WWW_AUTHENTICATE));
    }

    @Test
    void wrongPasswordGives401() {
        Response response = call("GET", "/invoices", basic("lokesh", "wrong-pass"));
        assertEquals(401, response.getStatus());
    }

    @Test
    void userCanReadInvoices() {
        Response response = call("GET", "/invoices/1", basic("lokesh", "user-demo-pass"));
        assertEquals(200, response.getStatus());
    }

    @Test
    void userWithoutAdminRoleGets403() {
        Response response = call("DELETE", "/invoices/1", basic("lokesh", "user-demo-pass"));
        assertEquals(403, response.getStatus());
    }

    @Test
    void adminCanDeleteInvoice() {
        Response response = call("DELETE", "/invoices/2", basic("alex", "admin-demo-pass"));
        assertEquals(204, response.getStatus());
    }

    @Test
    void optionalAuthTreatsBadCredentialsAsGuest() {
        Response response = call("GET", "/welcome", basic("lokesh", "wrong-pass"));
        assertEquals("Welcome, guest", response.readEntity(String.class));
    }
}
