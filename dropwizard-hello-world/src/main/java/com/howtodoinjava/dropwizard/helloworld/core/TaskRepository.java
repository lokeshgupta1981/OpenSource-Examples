package com.howtodoinjava.dropwizard.helloworld.core;

import com.howtodoinjava.dropwizard.helloworld.api.Task;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class TaskRepository {

    private final Map<Long, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicLong ids = new AtomicLong();

    public List<Task> findAll() {
        return tasks.values().stream().sorted(Comparator.comparingLong(Task::id)).toList();
    }

    public Optional<Task> findById(long id) {
        return Optional.ofNullable(tasks.get(id));
    }

    public Task save(String title) {
        Task task = new Task(ids.incrementAndGet(), title, false);
        tasks.put(task.id(), task);
        return task;
    }

    public Optional<Task> markDone(long id) {
        return Optional.ofNullable(tasks.computeIfPresent(id, (key, old) -> new Task(key, old.title(), true)));
    }

    public boolean delete(long id) {
        return tasks.remove(id) != null;
    }

    public int count() {
        return tasks.size();
    }
}
