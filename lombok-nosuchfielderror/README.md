Source code for the article https://howtodoinjava.com/lombok/lombok-nosuchfielderror-error/

# Lombok NoSuchFieldError: JCTree$JCImport qualid

A one-class Maven project that reproduces the build error an old Lombok throws on JDK 21 and newer,
and fixes it by moving to the latest Lombok. The *lombok.version* property (default 1.18.48) is used
in both places that matter, the dependency and the *annotationProcessorPaths* of the compiler plugin.

## Versions

- JDK 25 (Temurin 25.0.4.1), also run on JDK 21 to show the original message
- Maven 3.9.16, maven-compiler-plugin 3.16.0, maven-surefire-plugin 3.6.0
- Lombok 1.18.48 (fix), Lombok 1.18.28 (reproduces the error)
- JUnit 6.1.3

## Contents

- *Recipe* is a *@Data* class with two fields.
- *RecipeTest* checks the generated getters, setters and *toString()*.

## Run

```bash
# The fix: latest Lombok, build passes
mvn clean test

# Reproduce the error with Lombok 1.18.28 (profile or property, same result)
mvn clean compile -Pold-lombok
mvn clean compile -Dlombok.version=1.18.28

# On JDK 21 the old Lombok prints the JCImport message
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 mvn clean compile -Pold-lombok -Dmaven.compiler.release=21

# Which Lombok version is on the classpath
mvn dependency:tree -Dincludes=org.projectlombok
```

## What you see

| JDK | Lombok | Result |
|---|---|---|
| 21 | 1.18.28 | Fatal error compiling: java.lang.NoSuchFieldError: Class com.sun.tools.javac.tree.JCTree$JCImport does not have member field 'com.sun.tools.javac.tree.JCTree qualid' |
| 25 | 1.18.28 or 1.18.30 | Fatal error compiling: java.lang.ExceptionInInitializerError: com.sun.tools.javac.code.TypeTag :: UNKNOWN |
| 25 | 1.18.48 | BUILD SUCCESS, 1 test passes |
