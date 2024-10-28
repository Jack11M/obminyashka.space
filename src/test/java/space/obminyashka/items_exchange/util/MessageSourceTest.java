package space.obminyashka.items_exchange.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.web.servlet.MockMvc;
import space.obminyashka.items_exchange.rest.basic.BasicControllerTest;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.MessageSourceProxy.getParametrizedMessageSource;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_ENUM_VALUE;

@SpringBootTest
@AutoConfigureMockMvc
class MessageSourceTest extends BasicControllerTest {

    @Autowired
    public MessageSourceTest(MockMvc mockMvc) {
        super(mockMvc);
    }

    @Test
    void testMessagesFileAccessibility() {
        ClassPathResource classPathResource = new ClassPathResource("messages.properties");
        assertTrue(classPathResource.exists());

        String messageSource = getMessageSource(ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL);
        assertTrue(messageSource.contains("enter valid email address"));

        String parametrizedMessageSource = getParametrizedMessageSource(INVALID_ENUM_VALUE, "test-value");
        assertTrue(parametrizedMessageSource.contains("not found"));
    }
}
