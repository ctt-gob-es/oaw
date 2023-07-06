#!/bin/bash

## Variables
jdk_route=/usr/lib/jvm/
jdk_version=java-8-openjdk-amd64

export JAVA_HOME=$jdk_route$jdk_version

## War Generation
mvn  -f ./oaw/pom.xml clean install -P docker -Dmaven.test.skip=true
    
## Run containers
docker compose -f ./docker/docker-compose.yml up -d --build