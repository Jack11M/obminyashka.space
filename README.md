# Obminyashka (Child Goods Exchange)

This is the source code of the IT School Hillel's EVO project "Obminyashka".

Our aim is to give our users opportunity to share or exchange any children's clothes.

## The technologies used:

#### Front-End

- React (Router, Redux)
- Styled-Components

#### Back-End

- Java, Spring (Boot, Web MVC, Data JPA, Security), JWT, Lombok
- MySQL, H2, Liquibase, Database-Rider
- Swagger
- Docker

## Usage

### Requirements for manual build

- [OpenJDK](https://openjdk.java.net/projects/jdk/19/) `version 19`
- [NGINX](https://nginx.org) `version 1.18.0`
- [MySQL](https://www.mysql.com/downloads/) `version 8` ( scheme `evo_exchange` will be created after the very first local run )
- [maven](https://maven.apache.org/index.html) `version 3.8.1`

### Build & Run

#### Completely automated Docker container build

Install [docker](https://www.docker.com/get-started) to run the project.
For running the project use Docker Compose v2 and enter following:

```bash
docker compose up --build
```

and once you finished:

```bash
docker compose down
```

#### Manual build with custom parameters

1. Install [openssl](https://www.openssl.org) and for generation local SSL certificate and key enter one by one into
   command line app following:

```bash
openssl req -x509 -newkey rsa:4096 -sha256 -days 365 -nodes -keyout obminyashka.key -out obminyashka.crt
openssl pkcs12 -export -in obminyashka.crt -inkey obminyashka.key -out keystore.p12 -name tomcat -caname root -passout pass:your_keystore_pass
```

2. Install [nginx](https://nginx.org/en/download.html) and copy [default.conf](nginx/conf.d/default.conf) content replacing `http { ... }` section of `ngix.conf` or replace `default.conf` file (if you're on Linux)
3. Replace string `web-server` in the config file with `localhost`
4. Replace path to previously created SSL certificate and key into `ssl_certificate` and `ssl_certificate_key` sections resp
   of the previously copied nginx configuration file.
5. Reload changes into nginx.

   - **Ubuntu**: `systemctl reload nginx`
   - **Windows**: `nginx -s reload` or with Explorer:`Win+R -> services.msc -> nginx (Restart)`

6. Use a project builder [maven](https://maven.apache.org/index.html) to install and run the project
   (check before run into [application.properties](src/main/resources/application.properties) all required properties)

```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run-arguments="\
--SSL_PATH=path_to_keystore.p12 \
--SSL_PASSWORD='your_keystore_pass' \
--DB_PASSWORD='your_db_pass' \
--app.jwt.secret='your_jwt_pass'"
```

## Obminyashka URL

### Main page

Server: https://obminyashka.space

Localhost: https://localhost

### API

Server: https://obminyashka.space/swagger-ui/index.html

Localhost URL: https://localhost/swagger-ui/index.html

## Top 5 Contributors

##### Thanks to the following people, and many other who have contributed to this project:

- [@Wolshebnik](https://github.com/Wolshebnik)
- [@rpkyrych](https://github.com/rpkyrych)
- [@Jack11M](https://github.com/Jack11M)
- [@cloudmenson](https://github.com/cloudmenson)
- [@smilaya](https://github.com/smilaya)
