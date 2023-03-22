package space.obminyashka.items_exchange.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@Getter
@Setter
public class ChangeEmailRequest {

    @Parameter(name = "email", description = "New email")
    @NotEmpty(message = "{" + EMPTY_PASSWORD + "}")
    @Email(regexp = PatternHandler.EMAIL, message = "{" + INVALID_EMAIL + "}")
    private String email;
}



