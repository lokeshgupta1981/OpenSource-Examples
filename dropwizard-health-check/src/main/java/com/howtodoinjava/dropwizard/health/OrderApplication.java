package com.howtodoinjava.dropwizard.health;

import com.howtodoinjava.dropwizard.health.core.InventoryDatabase;
import com.howtodoinjava.dropwizard.health.health.DatabaseHealthCheck;
import com.howtodoinjava.dropwizard.health.health.PaymentGatewayHealthCheck;
import com.howtodoinjava.dropwizard.health.resources.GatewayStatusResource;
import com.howtodoinjava.dropwizard.health.resources.OrderResource;
import com.howtodoinjava.dropwizard.health.resources.StatusResource;
import com.howtodoinjava.dropwizard.health.tasks.DatabaseOutageTask;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Environment;

public class OrderApplication extends Application<OrderConfiguration> {

    public static void main(String[] args) throws Exception {
        new OrderApplication().run(args);
    }

    @Override
    public String getName() {
        return "order-service";
    }

    @Override
    public void run(OrderConfiguration config, Environment environment) {
        InventoryDatabase database = new InventoryDatabase();

        environment.jersey().register(new OrderResource());
        environment.jersey().register(new GatewayStatusResource());
        environment.jersey().register(new StatusResource(environment.healthChecks()));

        environment.healthChecks().register("database", new DatabaseHealthCheck(database));
        environment.healthChecks().register("payment-gateway",
                new PaymentGatewayHealthCheck(config.getPaymentGatewayUrl()));

        environment.admin().addTask(new DatabaseOutageTask(database));
    }
}
