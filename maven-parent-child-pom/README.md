Source code for the article https://howtodoinjava.com/maven/maven-parent-child-pom-example/

# Maven parent and child POM example

A multi-module library catalog. The parent POM (`catalog-parent`, packaging `pom`) holds the shared properties, dependency versions, plugin versions and the JUnit test dependency. The two child modules inherit them.

| Module | What it shows |
|---|---|
| catalog-core | A child POM with only `<parent>` and `<artifactId>`. `groupId`, `version`, properties, the JUnit dependency and the compiler settings come from the parent. |
| catalog-app | A child POM that adds a dependency on `catalog-core` without a version (from `dependencyManagement`) and configures `maven-jar-plugin` without a version (from `pluginManagement`). |

## Versions

- Maven 3.9.16, POM model 4.0.0
- Java 25 (compiled with `--release 25`)
- JUnit 6.1.3 (junit-bom), maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0, maven-jar-plugin 3.5.1

## Run

```bash
# Build both modules and run the tests
mvn clean test

# Show the effective POM of one child (parent plus Super POM merged in)
mvn -pl catalog-app help:effective-pom

# Build only catalog-app and the modules it depends on
mvn -pl catalog-app -am package
```
