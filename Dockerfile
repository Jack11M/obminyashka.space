FROM maven:3.8.6-amazoncorretto-18
COPY pom.xml /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN mvn clean install
