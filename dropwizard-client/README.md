Source code for the article https://howtodoinjava.com/dropwizard/client-configuration-and-examples/

An order service that calls an inventory service with the Dropwizard Jersey client (`JerseyClientBuilder`)
and with Apache HttpClient 5 (`HttpClientBuilder`). To keep the demo in one jar, the app serves the
inventory endpoints (`/inventory`) too, and `inventoryUrl` in `config.yml` points back to it.

Versions: Java 25 (Dropwizard 5 needs Java 17 or later), Dropwizard 5.0.2 (Jetty 12.1, Jersey 3.1,
Apache HttpClient 5.6), JUnit 6.1.3, Maven Shade plugin 3.6.2.

Build, test and run:

    mvn package
    java -jar target/dropwizard-client-1.0.0.jar server config.yml

    curl http://localhost:18090/orders/availability/coffee
    curl http://localhost:18090/orders/availability/coffee/http-client
    curl -X POST -H "Content-Type: application/json" -d '{"sku":"coffee","quantity":2}' http://localhost:18090/orders

Simulate a down inventory service:

    java -Ddw.inventoryUrl=http://localhost:18099 -jar target/dropwizard-client-1.0.0.jar server config.yml

HTTPS variant (`config-tls.yml`). Create the demo key store and trust store first (demo password only):

    mkdir tls
    keytool -genkeypair -alias inventory -keyalg EC -groupname secp256r1 -validity 3650 -dname "CN=localhost" \
      -ext "SAN=dns:localhost,ip:127.0.0.1" -storetype PKCS12 -keystore tls/inventory-server.p12 -storepass changeit
    keytool -exportcert -alias inventory -keystore tls/inventory-server.p12 -storepass changeit -file tls/inventory.crt
    keytool -importcert -noprompt -alias inventory -file tls/inventory.crt -storetype PKCS12 \
      -keystore tls/client-truststore.p12 -storepass changeit
    java -jar target/dropwizard-client-1.0.0.jar server config-tls.yml
    curl -k https://localhost:18092/orders/availability/coffee
