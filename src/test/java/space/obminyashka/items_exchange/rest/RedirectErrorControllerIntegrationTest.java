package space.obminyashka.items_exchange.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedirectErrorControllerIntegrationTest {
    @Autowired
    private TestRestTemplate template;

    @Test
    void redirectError_shouldReturn404AndFallTrough_whenUrlNotFound() {

        ResponseEntity<String> response = template.getForEntity("/user/page-not-exist", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        final var body = response.getBody();
        assertNotNull(body);
        assertTrue(body.contains("\"path\":\"/user/page-not-exist\""), "ErrorController has to pass URL and 404 to React part");
    }
}