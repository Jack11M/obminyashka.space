package space.obminyashka.items_exchange.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.rest.regexp.PatternHandler;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest {

    @Schema(example = "string")
    @NotEmpty(message = "{" + EMPTY_USERNAME + "}")
    @Size(min = 2, max = 50, message = "{" + INVALID_USERNAME_SIZE + "}")
    @Pattern(regexp = PatternHandler.USERNAME, message = "{" + INVALID_USERNAME + "}")
    private String username;

    @NotEmpty(message = "{" + EMPTY_EMAIL + "}")
    @Email(regexp = PatternHandler.EMAIL, message = "{" + INVALID_EMAIL + "}")
    private String email;

    @Schema(example = "string")
    @NotEmpty(message = "{" + EMPTY_PASSWORD + "}")
    @Size(min = 8, max = 30, message = "{" + INVALID_PASSWORD_SIZE + "}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{" + INVALID_PASSWORD + "}")
    private String password;

    @NotEmpty(message = "{" + EMPTY_CONFIRM_PASS + "}")
    private String confirmPassword;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + DIFFERENT_PASSWORDS + "}")
    private boolean isPasswordsEquals() {
        return password.equals(confirmPassword);
    }
}
