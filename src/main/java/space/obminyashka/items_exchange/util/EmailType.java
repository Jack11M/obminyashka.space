package space.obminyashka.items_exchange.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailType {
    REGISTRATION("registration.header", "registration.body");

    public final String header;
    public final String body;
}
