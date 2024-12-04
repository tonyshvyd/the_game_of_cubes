#!/usr/bin/env bash

mvn clean compile assembly:single && \
java -jar ./target/debug_client-1.0-jar-with-dependencies.jar