package com.howtodoinjava.dropwizard.helloworld;

import com.howtodoinjava.dropwizard.helloworld.api.NewTask;
import com.howtodoinjava.dropwizard.helloworld.api.Task;
import com.howtodoinjava.dropwizard.helloworld.core.TaskRepository;
import com.howtodoinjava.dropwizard.helloworld.resources.TaskResource;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(DropwizardExtensionsSupport.class)
class TaskResourceTest {

    private static final TaskRepository REPOSITORY = new TaskRepository();

    static final ResourceExtension RESOURCES = ResourceExtension.builder()
            .addResource(new TaskResource(REPOSITORY))
            .build();

    @Test
    void createsTask() {
        Response response = RESOURCES.target("/tasks").request()
                .post(Entity.json(new NewTask("Buy milk")));

        assertEquals(201, response.getStatus());
        assertEquals("Buy milk", response.readEntity(Task.class).title());
    }

    @Test
    void rejectsBlankTitle() {
        Response response = RESOURCES.target("/tasks").request()
                .post(Entity.json(new NewTask(" ")));

        assertEquals(422, response.getStatus());
        assertTrue(response.readEntity(String.class).contains("title must not be blank"));
    }

    @Test
    void returns404ForUnknownTask() {
        Response response = RESOURCES.target("/tasks/99").request().get();

        assertEquals(404, response.getStatus());
    }
}
