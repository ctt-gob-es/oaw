#!/bin/bash

## Variables
jdk_route=/usr/lib/jvm/
jdk_version=java-8-openjdk-amd64
subject='/C=XX/ST=State/L=Org/O=example.com'

export JAVA_HOME=$jdk_route$jdk_version

## Nginx certificate generation
if ! [ -f "./motor-js/nginx/certs/server.key" -a -f "./motor-js/nginx/certs/server.crt" ]; then

 # Removing possible empty folders with exclusive names
 sudo rmdir ./motor-js/nginx/certs/server.key >> /dev/null 2>&1
 sudo rmdir ./motor-js/nginx/certs/server.crt >> /dev/null 2>&1

 # Key file generation
 openssl genpkey -algorithm RSA -out ./motor-js/nginx/certs/server.key >> /dev/null 2>&1

 # Certificate file generation
 openssl req -new -key ./motor-js/nginx/certs/server.key -x509 -subj "$subject" -out ./motor-js/nginx/certs/server.crt

fi

# Removing possible empty folders with oaw.war name
sudo rmdir ./portal/target/oaw.war >> /dev/null 2>&1

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
