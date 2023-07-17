package space.obminyashka.items_exchange.rest.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import space.obminyashka.items_exchange.rest.regexp.PatternHandler;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

public record ChangePasswordRequest(@Parameter(name = "password", description = "New password")
                                    @NotEmpty(message = "{" + EMPTY_PASSWORD + "}")
                                    @Size(min = 8, max = 30, message = "{" + INVALID_PASSWORD_SIZE + "}")
                                    @Pattern(regexp = PatternHandler.PASSWORD, message = "{" + INVALID_PASSWORD + "}")
                                    String password,
                                    @Parameter(name = "confirmPassword", description = "Confirm new password")
                                    @NotEmpty(message = "{" + EMPTY_CONFIRM_PASS + "}") String confirmPassword) {

        @SuppressWarnings("unused")  // Used in validation process by Spring Validator
        @AssertTrue(message = "{" + DIFFERENT_PASSWORDS + "}")
        private boolean isPasswordsEquals() {
            return password.equals(confirmPassword);
        }
}
