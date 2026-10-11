package com.howtodoinjava.dropwizard.auth.core;

import java.security.Principal;
import java.util.Set;

public record User(String name, Set<String> roles) implements Principal {

    @Override
    public String getName() {
        return name;
    }
}
