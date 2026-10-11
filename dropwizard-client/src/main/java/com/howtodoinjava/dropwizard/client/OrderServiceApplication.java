package com.howtodoinjava.dropwizard.client;

import com.howtodoinjava.dropwizard.client.client.InventoryClient;
import com.howtodoinjava.dropwizard.client.client.InventoryHttpClient;
import com.howtodoinjava.dropwizard.client.client.InventoryUnavailableExceptionMapper;
import com.howtodoinjava.dropwizard.client.inventory.InventoryResource;
import com.howtodoinjava.dropwizard.client.resources.OrderResource;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;
import jakarta.ws.rs.client.Client;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;

public class OrderServiceApplication extends Application<OrderServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new OrderServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "order-service";
    }

    @Override
    public void run(OrderServiceConfiguration configuration, Environment environment) {
        Client client = new JerseyClientBuilder(environment)
                .using(configuration.getJerseyClientConfiguration())
                .build("inventory-client");

        CloseableHttpClient httpClient = new HttpClientBuilder(environment)
                .using(configuration.getHttpClientConfiguration())
                .build("inventory-http-client");

        String inventoryUrl = configuration.getInventoryUrl();
        InventoryClient inventoryClient = new InventoryClient(client, inventoryUrl);
        InventoryHttpClient inventoryHttpClient =
                new InventoryHttpClient(httpClient, environment.getObjectMapper(), inventoryUrl);

        environment.jersey().register(new InventoryResource());
        environment.jersey().register(new OrderResource(inventoryClient, inventoryHttpClient));
        environment.jersey().register(new InventoryUnavailableExceptionMapper());
    }
}
