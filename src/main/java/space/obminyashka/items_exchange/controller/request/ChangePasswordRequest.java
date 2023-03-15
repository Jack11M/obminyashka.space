package space.obminyashka.items_exchange.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;
@NoArgsConstructor
@Getter
@Setter
public class ChangePasswordRequest {

    @Parameter(name = "New password")
    @Size(min = 8, max = 30, message = "{" + INVALID_PASSWORD_SIZE + "}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{" + INVALID_PASSWORD + "}")
    private String password;

    @Parameter(name = "Confirm new password")
    private String confirmPassword;
}
