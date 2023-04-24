FROM maven:3.9-eclipse-temurin-19
COPY pom.xml /tmp/
COPY src /tmp/src/
COPY lombok.config /tmp/
WORKDIR /tmp/
RUN mvn clean install
