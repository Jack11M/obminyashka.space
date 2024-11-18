package space.obminyashka.items_exchange.rest;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;
import space.obminyashka.items_exchange.repository.model.chat.Greeting;
import space.obminyashka.items_exchange.repository.model.chat.HelloMessage;

import static space.obminyashka.items_exchange.rest.api.ApiKey.CHAT_ID;

@Controller
public class ChatController {

    @MessageMapping(CHAT_ID)
    @SendTo("/topic/greetings/{chatId}")
    public Greeting greet(@DestinationVariable String chatId, HelloMessage message) {
        return new Greeting("Hello, " +
                HtmlUtils.htmlEscape(message.getName()));
    }

}
