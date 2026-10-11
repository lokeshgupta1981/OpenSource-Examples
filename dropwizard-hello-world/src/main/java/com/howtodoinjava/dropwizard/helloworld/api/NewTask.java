package com.howtodoinjava.dropwizard.helloworld.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewTask(@NotBlank @Size(max = 80) String title) {
}
