FROM maven:3.6.3-jdk-11-slim

ADD target/evoApp-*.jar evoApp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "evoApp.jar"]