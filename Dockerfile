FROM maven:3.6.3-jdk-11-slim
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install