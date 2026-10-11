Source code for the article https://howtodoinjava.com/dropwizard/dropwizard-basic-auth-security-example/

An invoice API secured with HTTP Basic authentication (dropwizard-auth): a BCrypt-based `Authenticator`,
a role `Authorizer`, `CachingAuthenticator`, `@Auth`, `@PermitAll`, `@RolesAllowed` and an optional `@Auth`.
Passwords are stored only as BCrypt hashes (at.favre.lib:bcrypt 0.10.2) in `config.yml`.

Versions: Java 25 (Dropwizard 5 needs Java 17 or later), Dropwizard 5.0.2 (Jetty 12.1, Jersey 3.1),
JUnit 6.1.3, Maven Shade plugin 3.6.2.

Build, test and run:

    mvn package
    java -jar target/dropwizard-basic-auth-1.0.0.jar hash-password     # prints a BCrypt hash for config.yml
    java -jar target/dropwizard-basic-auth-1.0.0.jar server config.yml

Demo users (demo passwords only, never use them anywhere else):

    curl -i http://localhost:18094/invoices                                      # 401
    curl -u lokesh:user-demo-pass http://localhost:18094/invoices                # 200
    curl -i -u lokesh:user-demo-pass -X DELETE http://localhost:18094/invoices/2 # 403
    curl -i -u alex:admin-demo-pass -X DELETE http://localhost:18094/invoices/2  # 204

Basic credentials are only Base64 encoded, so use HTTPS outside localhost.
