package com.howtodoinjava.dropwizard.auth;

import com.howtodoinjava.dropwizard.auth.cli.HashPasswordCommand;
import com.howtodoinjava.dropwizard.auth.core.BCryptAuthenticator;
import com.howtodoinjava.dropwizard.auth.core.RoleAuthorizer;
import com.howtodoinjava.dropwizard.auth.core.User;
import com.howtodoinjava.dropwizard.auth.resources.InvoiceResource;
import com.howtodoinjava.dropwizard.auth.resources.WelcomeResource;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.CachingAuthenticator;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

public class InvoiceApplication extends Application<InvoiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new InvoiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "invoice-service";
    }

    @Override
    public void initialize(Bootstrap<InvoiceConfiguration> bootstrap) {
        bootstrap.addCommand(new HashPasswordCommand());
    }

    @Override
    public void run(InvoiceConfiguration configuration, Environment environment) {
        BCryptAuthenticator authenticator = new BCryptAuthenticator(configuration.getUsers());
        CachingAuthenticator<BasicCredentials, User> cachingAuthenticator = new CachingAuthenticator<>(
                environment.metrics(), authenticator, configuration.getAuthenticationCachePolicy());

        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(cachingAuthenticator)
                        .setAuthorizer(new RoleAuthorizer())
                        .setRealm("invoices")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        environment.jersey().register(new InvoiceResource());
        environment.jersey().register(new WelcomeResource());
    }
}
