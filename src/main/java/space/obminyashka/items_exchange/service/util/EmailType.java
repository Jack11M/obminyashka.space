package space.obminyashka.items_exchange.service.util;

import lombok.RequiredArgsConstructor;
import space.obminyashka.items_exchange.rest.api.ApiKey;

@RequiredArgsConstructor
public enum EmailType {
    REGISTRATION("d-11d830a2e530404a8c9b9f5412a2034f", ApiKey.EMAIL_VALIDATE_CODE),
    CHANGING("d-11d830a2e530404a8c9b9f5412a2034f", ApiKey.EMAIL_VALIDATE_CODE),
    RESET("d-11d830a2e530404a8c9b9f5412a2034f", ApiKey.USER_SERVICE_PASSWORD_CONFIRM);

    public final String template;
    public final String callbackEndpoint;
}
