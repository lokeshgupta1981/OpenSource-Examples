package com.howtodoinjava.dropwizard.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.howtodoinjava.dropwizard.auth.core.UserAccount;
import io.dropwizard.core.Configuration;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class InvoiceConfiguration extends Configuration {

    @Valid
    @NotEmpty
    private List<UserAccount> users = List.of();

    @NotNull
    private CaffeineSpec authenticationCachePolicy = CaffeineSpec.parse("maximumSize=1000, expireAfterAccess=10m");

    @JsonProperty
    public List<UserAccount> getUsers() {
        return users;
    }

    @JsonProperty
    public void setUsers(List<UserAccount> users) {
        this.users = users;
    }

    @JsonProperty
    public CaffeineSpec getAuthenticationCachePolicy() {
        return authenticationCachePolicy;
    }

    @JsonProperty
    public void setAuthenticationCachePolicy(CaffeineSpec authenticationCachePolicy) {
        this.authenticationCachePolicy = authenticationCachePolicy;
    }
}
