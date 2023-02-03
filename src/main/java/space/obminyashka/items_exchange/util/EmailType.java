package space.obminyashka.items_exchange.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailType {
    REGISTRATION(ResponseMessagesHandler.PositiveMessage.EMAIL_REGISTRATION_HEADER,
            ResponseMessagesHandler.PositiveMessage.EMAIL_REGISTRATION_BODY);

    public final String header;
    public final String body;
}
