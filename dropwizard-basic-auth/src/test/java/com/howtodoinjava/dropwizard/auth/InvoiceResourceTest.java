package com.howtodoinjava.dropwizard.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.howtodoinjava.dropwizard.auth.core.BCryptAuthenticator;
import com.howtodoinjava.dropwizard.auth.core.RoleAuthorizer;
import com.howtodoinjava.dropwizard.auth.core.User;
import com.howtodoinjava.dropwizard.auth.core.UserAccount;
import com.howtodoinjava.dropwizard.auth.resources.InvoiceResource;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(DropwizardExtensionsSupport.class)
class InvoiceResourceTest {

    // Cost 4 keeps the test fast; production hashes use cost 12
    private static final String HASH = BCrypt.withDefaults().hashToString(4, "test-pass".toCharArray());

    private static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new BCryptAuthenticator(List.of(
                            new UserAccount("tester", HASH, Set.of("USER")))))
                    .setAuthorizer(new RoleAuthorizer())
                    .setRealm("invoices")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new InvoiceResource())
            .build();

    private static String basic(String username, String password) {
        String token = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(token.getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void readsInvoiceWithValidCredentials() {
        Response response = RESOURCES.target("/invoices/1").request()
                .header(HttpHeaders.AUTHORIZATION, basic("tester", "test-pass"))
                .get();
        assertEquals(200, response.getStatus());
    }

    @Test
    void deniesDeleteWithoutAdminRole() {
        Response response = RESOURCES.target("/invoices/1").request()
                .header(HttpHeaders.AUTHORIZATION, basic("tester", "test-pass"))
                .delete();
        assertEquals(403, response.getStatus());
    }

    @Test
    void rejectsRequestWithoutCredentials() {
        Response response = RESOURCES.target("/invoices").request().get();
        assertEquals(401, response.getStatus());
    }
}
