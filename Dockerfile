FROM maven:3.8.7-openjdk-18-slim
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install
