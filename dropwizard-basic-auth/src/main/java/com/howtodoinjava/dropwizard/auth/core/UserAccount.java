package com.howtodoinjava.dropwizard.auth.core;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

/** One entry of the users list in config.yml. The password is stored only as a BCrypt hash. */
public record UserAccount(@NotEmpty String username, @NotEmpty String passwordHash, Set<String> roles) {
}
