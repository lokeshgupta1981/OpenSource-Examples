#!/usr/bin/env bash
# Builds the example projects with Maven 3 and Maven 4 and keeps each log in ./logs.
# Usage: MAVEN3_HOME=/path/to/apache-maven-3.10.0 MAVEN4_HOME=/path/to/apache-maven-4.0.0-rc-7 ./compare-builds.sh
set -u
cd "$(dirname "$0")"
mkdir -p logs

run() {
  local label=$1 dir=$2 mvn=$3
  shift 3
  (cd "$dir" && "$mvn" -B "$@") > "logs/$label.log" 2>&1
  echo "$label: exit code $?"
}

run maven3-project-on-maven3 maven3-project "$MAVEN3_HOME/bin/mvn" clean verify
run maven3-project-on-maven4 maven3-project "$MAVEN4_HOME/bin/mvn" clean verify
run maven4-project-on-maven4 maven4-project "$MAVEN4_HOME/bin/mvn" clean install
run maven4-project-on-maven3 maven4-project "$MAVEN3_HOME/bin/mvn" clean verify
run legacy-pom-on-maven3     legacy-pom-problems "$MAVEN3_HOME/bin/mvn" validate
run legacy-pom-on-maven4     legacy-pom-problems "$MAVEN4_HOME/bin/mvn" validate
