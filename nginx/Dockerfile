FROM nginx:1.24
RUN apt install openssl
WORKDIR /etc/ssl/certs/
RUN openssl req -x509 -newkey rsa:4096 -sha256 -days 185 -nodes -keyout obminyashka.key -out obminyashka.crt -subj "/CN=localhost"
RUN openssl pkcs12 -export -in obminyashka.crt -inkey obminyashka.key -out keystore.p12 -name tomcat -caname root -passout pass:root
