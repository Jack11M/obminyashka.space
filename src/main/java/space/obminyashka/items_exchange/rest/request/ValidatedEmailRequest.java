package space.obminyashka.items_exchange.rest.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Email;
import space.obminyashka.items_exchange.rest.regexp.PatternHandler;
import space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler;

public record ValidatedEmailRequest(@Parameter(name = "user email", description = "email for reset password")
                                    @Email(regexp = PatternHandler.EMAIL , message = ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL)
                                    String email) {
}
