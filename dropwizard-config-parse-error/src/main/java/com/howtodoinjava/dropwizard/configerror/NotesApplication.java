package com.howtodoinjava.dropwizard.configerror;

import io.dropwizard.core.Application;
import io.dropwizard.core.Configuration;
import io.dropwizard.core.setup.Environment;

public class NotesApplication extends Application<Configuration> {

    public static void main(String[] args) throws Exception {
        new NotesApplication().run(args);
    }

    @Override
    public String getName() {
        return "notes-service";
    }

    @Override
    public void run(Configuration config, Environment environment) {
        environment.jersey().register(new NotesResource());
    }
}
