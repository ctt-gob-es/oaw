#!/bin/bash

## Variables
jdk_route=/usr/lib/jvm/
jdk_version=java-8-openjdk-amd64

export JAVA_HOME=$jdk_route$jdk_version

## War Generation
mvn  -f ./oaw/pom.xml clean install -P docker -Dmaven.test.skip=true

## Run containers with Docker version syntax control
docker compose -f ./docker/docker-compose.yml down
if [ $? -ne 0 ]; then
  docker-compose -f ./docker/docker-compose.yml down
fi

docker compose -f ./docker/docker-compose.yml up -d --build
if [ $? -ne 0 ]; then
  docker-compose -f ./docker/docker-compose.yml up -d --build
fi