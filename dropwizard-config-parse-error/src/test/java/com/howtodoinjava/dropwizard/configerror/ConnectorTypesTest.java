package com.howtodoinjava.dropwizard.configerror;

import io.dropwizard.jetty.ConnectorFactory;
import io.dropwizard.jetty.HttpConnectorFactory;
import io.dropwizard.jetty.HttpsConnectorFactory;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.ServiceLoader;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ConnectorTypesTest {

    @Test
    void httpAndHttpsConnectorsAreRegisteredAsServices() {
        List<Class<?>> types = ServiceLoader.load(ConnectorFactory.class).stream()
                .<Class<?>>map(ServiceLoader.Provider::type)
                .toList();

        assertTrue(types.contains(HttpConnectorFactory.class));
        assertTrue(types.contains(HttpsConnectorFactory.class));
    }
}
