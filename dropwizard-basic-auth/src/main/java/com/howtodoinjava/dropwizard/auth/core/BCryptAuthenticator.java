package com.howtodoinjava.dropwizard.auth.core;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BCryptAuthenticator implements Authenticator<BasicCredentials, User> {

    // Checked when the user name is unknown, so unknown and known users take the same time
    private static final String DUMMY_HASH = BCrypt.withDefaults().hashToString(12, "not-a-password".toCharArray());

    private final Map<String, UserAccount> accounts;

    public BCryptAuthenticator(List<UserAccount> users) {
        this.accounts = users.stream()
                .collect(Collectors.toUnmodifiableMap(UserAccount::username, Function.identity()));
    }

    @Override
    public Optional<User> authenticate(BasicCredentials credentials) {
        UserAccount account = accounts.get(credentials.getUsername());
        String hash = account != null ? account.passwordHash() : DUMMY_HASH;
        boolean matches = BCrypt.verifyer()
                .verify(credentials.getPassword().toCharArray(), hash)
                .verified;
        if (account == null || !matches) {
            return Optional.empty();
        }
        return Optional.of(new User(account.username(), account.roles()));
    }
}
