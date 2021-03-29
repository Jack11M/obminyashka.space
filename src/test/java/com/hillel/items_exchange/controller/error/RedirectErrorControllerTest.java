package com.hillel.items_exchange.controller.error;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedirectErrorControllerTest {
    @Autowired
    private TestRestTemplate template;

    @Test
    void redirectError_shouldRedirectAndDisplayIndexPage_when404codeReceived() throws Exception {
        String indexPagePath = "src/main/resources/react/build/index.html";
        String indexPageContent = new String(Files.readAllBytes(Path.of(indexPagePath)));

        ResponseEntity<String> response = template.getForEntity("/page-not-exist", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(indexPageContent, response.getBody(), "index.html has to be returned from ErrorController");
    }
}