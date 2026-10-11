package com.howtodoinjava.dropwizard.auth.cli;

import at.favre.lib.crypto.bcrypt.BCrypt;
import io.dropwizard.core.cli.Command;
import io.dropwizard.core.setup.Bootstrap;
import net.sourceforge.argparse4j.inf.Namespace;
import net.sourceforge.argparse4j.inf.Subparser;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;
import java.util.Arrays;

/** Prints a BCrypt hash for the users list in config.yml: java -jar app.jar hash-password */
public class HashPasswordCommand extends Command {

    public HashPasswordCommand() {
        super("hash-password", "Reads a password from the console and prints its BCrypt hash");
    }

    @Override
    public void configure(Subparser subparser) {
        // no arguments, so the password never ends up in the shell history
    }

    @Override
    public void run(Bootstrap<?> bootstrap, Namespace namespace) throws Exception {
        Console console = System.console();
        char[] password = console != null && console.isTerminal()
                ? console.readPassword("Password: ")
                : new BufferedReader(new InputStreamReader(System.in)).readLine().toCharArray();
        String hash = BCrypt.withDefaults().hashToString(12, password);
        Arrays.fill(password, ' ');
        System.out.println(hash);
    }
}
