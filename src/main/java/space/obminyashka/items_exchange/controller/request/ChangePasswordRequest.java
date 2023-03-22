package space.obminyashka.items_exchange.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@Getter
@Setter
public class ChangePasswordRequest {

    @Parameter(name = "password", description = "New password")
    @NotEmpty(message = "{" + EMPTY_PASSWORD + "}")
    @Size(min = 8, max = 30, message = "{" + INVALID_PASSWORD_SIZE + "}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{" + INVALID_PASSWORD + "}")
    private String password;

    @Parameter(name = "confirmPassword", description = "Confirm new password")
    @NotEmpty(message = "{" + EMPTY_CONFIRM_PASS + "}")
    private String confirmPassword;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + DIFFERENT_PASSWORDS + "}")
    private boolean isPasswordsEquals() {
        return password.equals(confirmPassword);
    }
}
