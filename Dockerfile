FROM maven:3.9.5-eclipse-temurin-21
COPY frontend /tmp/frontend
COPY pom.xml /tmp/
COPY src /tmp/src/
COPY lombok.config /tmp/
WORKDIR /tmp/
RUN mvn clean install -Dmaven.test.skip=true
