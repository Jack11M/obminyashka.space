package space.obminyashka.items_exchange.util;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum EmailType {
    REGISTRATION("d-d3d24de0a19f49afb06fc5505929b00c"),
    CHANGING("d-d3d24de0a19f49afb06fc5505929b00c");

    public final String template;
}
