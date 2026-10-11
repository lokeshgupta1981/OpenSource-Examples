package com.howtodoinjava.dropwizard.health.tasks;

import com.howtodoinjava.dropwizard.health.core.InventoryDatabase;
import io.dropwizard.servlets.tasks.Task;

import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

/** Admin task that switches the fake database off and on, for the demo. */
public class DatabaseOutageTask extends Task {

    private final InventoryDatabase database;

    public DatabaseOutageTask(InventoryDatabase database) {
        super("database-outage");
        this.database = database;
    }

    @Override
    public void execute(Map<String, List<String>> parameters, PrintWriter output) {
        boolean down = parameters.getOrDefault("down", List.of("true")).getFirst().equals("true");
        database.setReachable(!down);
        output.println(down ? "Inventory database is now unreachable" : "Inventory database is back");
    }
}
