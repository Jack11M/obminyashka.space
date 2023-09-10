package space.obminyashka.items_exchange.service.util;

import lombok.RequiredArgsConstructor;
import space.obminyashka.items_exchange.rest.api.ApiKey;

@RequiredArgsConstructor
public enum EmailType {
    REGISTRATION("d-d3d24de0a19f49afb06fc5505929b00c", ApiKey.EMAIL_VALIDATE_CODE),
    CHANGING("d-d3d24de0a19f49afb06fc5505929b00c", ApiKey.EMAIL_VALIDATE_CODE),
    RESET("d-d3d24de0a19f49afb06fc5505929b00c", ApiKey.USER_SERVICE_PASSWORD_CONFIRM);

    public final String template;
    public final String callbackEndpoint;
}
