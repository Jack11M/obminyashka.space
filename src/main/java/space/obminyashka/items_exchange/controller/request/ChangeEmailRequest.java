package space.obminyashka.items_exchange.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import space.obminyashka.items_exchange.util.PatternHandler;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

public record ChangeEmailRequest(@Parameter(name = "email", description = "New email")
                                 @NotEmpty(message = "{" + EMPTY_EMAIL + "}")
                                 @Email(regexp = PatternHandler.EMAIL, message = "{" + INVALID_EMAIL + "}")
                                 String email) {
}
