FROM maven:3.6.3-jdk-8 AS MAVEN_TOOL_CHAIN

COPY common /tmp/common
COPY crawler /tmp/crawler
COPY intavcore /tmp/intavcore
COPY motor-js /tmp/motor-js
COPY oaw /tmp/oaw
COPY portal /tmp/portal

WORKDIR /tmp/oaw

ARG proxy_settings=""

RUN mvn clean install -P docker -DskipTests $proxy_settings


FROM tomcat:7.0-jdk8-openjdk-slim

COPY --from=MAVEN_TOOL_CHAIN /tmp/portal/target/oaw.war $CATALINA_HOME/webapps/oaw.war
#COPY deployment/docker/conf-context-oaw.xml $CATALINA_BASE/conf/Catalina/localhost/oaw.xml

RUN mkdir -m777 $CATALINA_BASE/conf/Catalina
RUN chmod -R 777 /usr/local/tomcat/webapps

ENV CATALINA_OPTS="-Dorg.apache.tomcat.util.digester.PROPERTY_SOURCE=org.apache.tomcat.util.digester.EnvironmentPropertySource"

ENV OAW_DB_NAME="oaw"
ENV OAW_DB_HOST="mysql"
ENV OAW_DB_PORT="3306"

ENV OAW_DB_USER="root"
ENV OAW_DB_PASS="root"
