package com.howtodoinjava.dropwizard.health;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.core.Configuration;
import jakarta.validation.constraints.NotNull;

import java.net.URI;

public class OrderConfiguration extends Configuration {

    @NotNull
    private URI paymentGatewayUrl;

    @JsonProperty
    public URI getPaymentGatewayUrl() {
        return paymentGatewayUrl;
    }

    @JsonProperty
    public void setPaymentGatewayUrl(URI paymentGatewayUrl) {
        this.paymentGatewayUrl = paymentGatewayUrl;
    }
}
