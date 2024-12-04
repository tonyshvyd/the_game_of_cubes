#!/usr/bin/env bash

mvn clean compile package && \
mvn install:install-file -Dfile=./target/comms_model-1.0.jar