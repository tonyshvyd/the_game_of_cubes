#!/usr/bin/env bash

mvn clean compile assembly:single && \
java -jar target/server-1.0-jar-with-depenedencies.jar

