package space.obminyashka.items_exchange.rest.request;

import jakarta.validation.constraints.Email;
import space.obminyashka.items_exchange.rest.regexp.PatternHandler;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

public record ValidatedEmailRequest(@Email(regexp = PatternHandler.EMAIL, message = "{" + INVALID_EMAIL + "}")
                                    String email) {
}

