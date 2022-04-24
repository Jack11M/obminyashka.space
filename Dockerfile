FROM maven:3.8.5-openjdk-17-slim
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install -Dmaven.test.skip=true
