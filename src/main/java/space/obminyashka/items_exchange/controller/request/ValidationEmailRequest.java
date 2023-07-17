package space.obminyashka.items_exchange.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import space.obminyashka.items_exchange.util.PatternHandler;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

public record ValidationEmailRequest(@Parameter(name = "email", description = "User email")
                                     @Email(regexp = PatternHandler.EMAIL, message = "{" + INVALID_EMAIL + "}")
                                     String email) {
}
