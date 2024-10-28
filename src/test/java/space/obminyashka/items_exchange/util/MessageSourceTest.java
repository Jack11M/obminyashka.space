package space.obminyashka.items_exchange.util;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MessageSourceTest {

    @Test
    void testMessagesFileAccessibility() {
        ClassPathResource classPathResource = new ClassPathResource("messages.properties");
        assertTrue(classPathResource.exists());
    }
}
