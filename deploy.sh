#!/bin/bash

## Variables
jdk_route=/usr/lib/jvm/
jdk_version=java-8-openjdk-amd64
subject='/C=XX/ST=State/L=Org/O=example.com'

export JAVA_HOME=$jdk_route$jdk_version

## Nginx certificate generation
if ! [ -f "./docker/nginx/certs/server.key" -a -f "./docker/nginx/certs/server.crt" ]; then

 # Removing empty folders with exclusive names
 sudo rmdir ./docker/nginx/certs/server.key >> /dev/null 2>&1
 sudo rmdir ./docker/nginx/certs/server.crt >> /dev/null 2>&1

 # Key file generation
 openssl genpkey -algorithm RSA -out ./docker/nginx/certs/server.key >> /dev/null 2>&1

 # Certificate file generation
 openssl req -new -key ./docker/nginx/certs/server.key -x509 -subj "$subject" -out ./docker/nginx/certs/server.crt

fi


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
