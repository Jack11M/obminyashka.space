FROM maven:3.8.7-eclipse-temurin-19
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install
