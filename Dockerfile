FROM gradle:7.0-jdk11
COPY build.gradle.kts /tmp/
COPY settings.gradle.kts /tmp/
COPY src /tmp/src/
WORKDIR /tmp/
RUN gradle build