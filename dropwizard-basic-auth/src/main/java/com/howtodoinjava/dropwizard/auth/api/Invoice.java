package com.howtodoinjava.dropwizard.auth.api;

import java.math.BigDecimal;

public record Invoice(long id, String customer, BigDecimal amount) {
}
