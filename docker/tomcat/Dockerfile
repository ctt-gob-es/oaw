FROM maven:3.8.1-jdk-8 as maven_builder
WORKDIR /app/code
COPY . .
WORKDIR /app/code/oaw
RUN mvn clean install -P docker -DskipTests

FROM tomcat:7.0.109-jdk8-openjdk
COPY --from=maven_builder /app/code/portal/target/oaw.war /usr/local/tomcat/webapps
