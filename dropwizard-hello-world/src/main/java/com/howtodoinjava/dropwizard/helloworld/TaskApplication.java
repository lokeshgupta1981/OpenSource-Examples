package com.howtodoinjava.dropwizard.helloworld;

import com.howtodoinjava.dropwizard.helloworld.core.TaskRepository;
import com.howtodoinjava.dropwizard.helloworld.health.TaskStoreHealthCheck;
import com.howtodoinjava.dropwizard.helloworld.resources.HelloResource;
import com.howtodoinjava.dropwizard.helloworld.resources.TaskResource;
import io.dropwizard.core.Application;
import io.dropwizard.core.setup.Bootstrap;
import io.dropwizard.core.setup.Environment;

public class TaskApplication extends Application<TaskConfiguration> {

    public static void main(String[] args) throws Exception {
        new TaskApplication().run(args);
    }

    @Override
    public String getName() {
        return "task-service";
    }

    @Override
    public void initialize(Bootstrap<TaskConfiguration> bootstrap) {
        // bundles and commands go here
    }

    @Override
    public void run(TaskConfiguration config, Environment environment) {
        TaskRepository repository = new TaskRepository();

        environment.jersey().register(new HelloResource(config.getTemplate(), config.getDefaultName()));
        environment.jersey().register(new TaskResource(repository));
        environment.healthChecks().register("task-store", new TaskStoreHealthCheck(repository));
    }
}
