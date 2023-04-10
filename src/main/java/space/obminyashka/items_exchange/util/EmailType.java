package space.obminyashka.items_exchange.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailType {

    REGISTRATION(ResponseMessagesHandler.PositiveMessage.EMAIL_REGISTRATION_TOPIC,
            "d-c03174487cde425695447cad3a3d5fd7");

    public final String topic;
    public final String template;
}
