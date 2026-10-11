package com.howtodoinjava.dropwizard.helloworld.health;

import com.codahale.metrics.health.HealthCheck;
import com.howtodoinjava.dropwizard.helloworld.core.TaskRepository;

public class TaskStoreHealthCheck extends HealthCheck {

    private final TaskRepository repository;

    public TaskStoreHealthCheck(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    protected Result check() {
        int count = repository.count();
        return Result.healthy("%d tasks in memory", count);
    }
}
