Source code for the article https://howtodoinjava.com/?p=44110

# Maven 4 migration example

The same multi-module playlist project, once as a Maven 3 build (POM model 4.0.0) and once as a Maven 4 build (POM model 4.1.0).

| Folder | What it shows |
|---|---|
| maven3-project | Model 4.0.0, `<modules>`, hard-coded parent versions, an execution bound to `post-clean`. Builds with Maven 3.9.x, 3.10.x and 4.0.0-rc-7. |
| maven4-project | Model 4.1.0, `root="true"`, `<subprojects>`, `<parent/>`, CI-friendly `revision` property, `bom` packaging, `before:integration-test[n]` and `after:clean` phases, a condition-based profile, flattened consumer POMs and the Maven Wrapper set to 4.0.0-rc-7. Builds with Maven 4 only. |
| legacy-pom-problems | A duplicate plugin declaration and maven-enforcer-plugin 1.0 (a Maven 2 plugin). Maven 3.9.16 builds it with a warning; Maven 3.10.0 and Maven 4 fail. |

## Versions

- Maven 3.10.0 (current stable) and 3.9.16 (previous stable line)
- Maven 4.0.0-rc-7 (release candidate; Maven 4.0.0 GA is not released yet)
- Java 17 or newer to run Maven 4 (tested with JDK 25), code compiled with `--release 17`
- JUnit 6.1.3, maven-compiler-plugin 3.16.0, maven-surefire-plugin and maven-failsafe-plugin 3.6.0, maven-wrapper-plugin 3.3.4

## Run

```bash
# Maven 3 build of the Maven 3 project
cd maven3-project
mvn clean verify

# Maven 4 build of the Maven 4 project, no Maven installation needed
cd ../maven4-project
./mvnw clean install

# Override the version from the command line (CI-friendly versions)
./mvnw clean install -Drevision=1.1.0

# Check what the Maven Upgrade Tool would change in the Maven 3 project
cd ../maven3-project
mvnup check --model-version 4.1.0 --all
```

To build everything with both Maven versions and keep the logs:

```bash
MAVEN3_HOME=/path/to/apache-maven-3.10.0 MAVEN4_HOME=/path/to/apache-maven-4.0.0-rc-7 ./compare-builds.sh
```

## Notes

- `maven4-project/.mvn/maven-user.properties` sets `maven.consumer.pom.flatten=true`. Without it, Maven 4.0.0-rc-7 refuses to install the
  parent, because its condition-based profile cannot be written as a model 4.0.0 consumer POM.
- `mvnup apply --model-version 4.1.0` moves `maven.compiler.release` into `<build><sources>`. Only maven-compiler-plugin 4.x reads that element,
  so with maven-compiler-plugin 3.x the build falls back to `-source 8`. Keep the property, or move to maven-compiler-plugin 4.0.0-beta-5.
