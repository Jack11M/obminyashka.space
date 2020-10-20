# Obmenyashka (Child Goods Exchange)

This is the back-end part of the IT School Hillel EVO project "Obmenyashka".

We want to give our users opportunity to share or exchange any children's clothes.  

The technologies used:
- Java, Spring (Boot, Web MVC, Data JPA, Security), JWT, Lombok 
- MySQL, Liquibase, Database-Rider
- Swagger
- Docker

## Usage
### Requirements

 - [JDK](https://www.oracle.com/technetwork/java/javase/downloads/index.html) `version 11`
 - [MySQL](https://www.mysql.com/downloads/) `version 8`
 - [maven](https://maven.apache.org/index.html) `version 3.6.+`
 - MySQL scheme `evo_exchange` will be created after the very first local run

### Installation

Use a project builder [maven](https://maven.apache.org/index.html) to install the project if you fulfil the requirements.
```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run-arguments=--p='your_local_DB_password'.
```
Otherwise, use [docker](https://www.docker.com/get-started) to run the project without installation.
```bash
docker-compose build
docker-compose up
```

### API
Use Swagger-UI local URL: `http://localhost:8080/swagger-ui/`

### Build

in progress 

### Run

in progress

## Contributors

##### Thanks to the following people who have contributed to this project:

- @Jack11M
- @KozminAlexandr
- @rpkyrych
- @lubchik-14
- @drw85
- @Wolshebnik
- @Dariy98
