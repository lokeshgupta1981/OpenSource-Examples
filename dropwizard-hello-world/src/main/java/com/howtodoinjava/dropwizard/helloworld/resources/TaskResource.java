package com.howtodoinjava.dropwizard.helloworld.resources;

import com.howtodoinjava.dropwizard.helloworld.api.NewTask;
import com.howtodoinjava.dropwizard.helloworld.api.Task;
import com.howtodoinjava.dropwizard.helloworld.core.TaskRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;

@Path("/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    private final TaskRepository repository;

    public TaskResource(TaskRepository repository) {
        this.repository = repository;
    }

    @GET
    public List<Task> findAll() {
        return repository.findAll();
    }

    @GET
    @Path("/{id}")
    public Task findById(@PathParam("id") long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Task " + id + " not found"));
    }

    @POST
    public Response create(@NotNull @Valid NewTask newTask) {
        Task task = repository.save(newTask.title());
        return Response.created(URI.create("/tasks/" + task.id())).entity(task).build();
    }

    @PUT
    @Path("/{id}/done")
    public Task markDone(@PathParam("id") long id) {
        return repository.markDone(id)
                .orElseThrow(() -> new NotFoundException("Task " + id + " not found"));
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        if (!repository.delete(id)) {
            throw new NotFoundException("Task " + id + " not found");
        }
        return Response.noContent().build();
    }
}
