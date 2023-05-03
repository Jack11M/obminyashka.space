FROM maven:3.8.7-eclipse-temurin-19
COPY frontend /tmp/frontend
COPY pom.xml /tmp/
COPY src /tmp/src/
COPY lombok.config /tmp/
WORKDIR /tmp/
RUN mvn clean install
