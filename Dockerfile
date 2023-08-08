FROM maven:3.9.3-eclipse-temurin-20
COPY frontend /tmp/frontend
COPY pom.xml /tmp/
COPY src /tmp/src/
COPY lombok.config /tmp/
WORKDIR /tmp/
RUN mvn clean install -Dmaven.test.skip=true
