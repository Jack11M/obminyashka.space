FROM maven:3.6.3-jdk-11-slim
COPY pom.xml /tmp/
COPY src /tmp/src/
COPY nginx/cert/*.p12 /tmp/
WORKDIR /tmp/
RUN mvn clean install