plugins {
    java
    `maven-publish`
    id("io.freefair.lombok") version "6.0.0-m2"
    id("org.siouan.frontend-jdk11") version "5.0.1"
}

frontend {
    nodeVersion.set("15.14.0")
    nodeInstallDirectory.set(file("${projectDir}/src/main/resources/react/node"))

    installScript.set("install")
    assembleScript.set("run build")
    packageJsonDirectory.set(file("${rootProject.projectDir}/src/main/resources/react"))

}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.3.9.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-security:2.3.9.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-web:2.3.9.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-validation:2.3.9.RELEASE")
    implementation("mysql:mysql-connector-java:8.0.23")
    implementation("org.liquibase:liquibase-core:4.3.1")
    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("org.projectlombok:lombok:1.18.18")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.11.4")
    implementation("org.modelmapper:modelmapper:2.3.9")
    implementation("io.springfox:springfox-boot-starter:3.0.0")
    runtimeOnly("com.h2database:h2:1.4.200")
    testImplementation("org.springframework.security:spring-security-test:5.3.8.RELEASE")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.3.9.RELEASE")
    testImplementation("org.junit.jupiter:junit-jupiter:5.6.3")
    testImplementation("com.github.database-rider:rider-junit5:1.23.0")
}

group = "space.obminyashka"
version = "0.0.1-SNAPSHOT"
description = "Obminyashka"
java.sourceCompatibility = JavaVersion.VERSION_11

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.named("processResources") {
    dependsOn(tasks.named("installFrontend"))
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.test{
    useJUnitPlatform()
}
