package space.obminyashka.items_exchange.service.util;

import lombok.RequiredArgsConstructor;
import space.obminyashka.items_exchange.rest.api.ApiKey;

@RequiredArgsConstructor
public enum EmailType {
    REGISTRATION("d-dd980dcd28fa4460a579959b12a7b68c", ApiKey.EMAIL_VALIDATE_CODE),
    CHANGING("d-dd980dcd28fa4460a579959b12a7b68c", ApiKey.EMAIL_VALIDATE_CODE),
    RESET_PASSWORD("d-dd980dcd28fa4460a579959b12a7b68c", ApiKey.USER_SERVICE_PASSWORD_CONFIRM);

    public final String template;
    public final String callbackEndpoint;
}
