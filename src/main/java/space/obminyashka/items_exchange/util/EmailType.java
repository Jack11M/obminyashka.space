package space.obminyashka.items_exchange.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailType {
    REGISTRATION(ResponseMessagesHandler.PositiveMessage.EMAIL_REGISTRATION_TOPIC,
            "d-d3d24de0a19f49afb06fc5505929b00c");

    public final String topic;
    public final String template;
}
