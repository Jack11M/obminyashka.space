package space.obminyashka.items_exchange.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import space.obminyashka.items_exchange.rest.regexp.PatternHandler;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL;

public record ValidationEmailRequest(@Schema(example = "email@gmail.com", description = "email for reset password")
                                     @Email(regexp = PatternHandler.EMAIL, message = "{" + INVALID_EMAIL + "}")
                                     String email) {
}
