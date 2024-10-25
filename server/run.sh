#!/usr/bin/env bash

mvn package && \
java -cp target/server-1.0-SNAPSHOT.jar:$HOME/.m2/repository/org/jetbrains/kotlin/kotlin-stdlib/2.0.10/kotlin-stdlib-2.0.10.jar the_game_of_cubes.MainKt

