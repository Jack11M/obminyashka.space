package space.obminyashka.items_exchange.dto;

import space.obminyashka.items_exchange.util.ResponseMessagesHandler;
import space.obminyashka.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_USERNAME)
    @Size(min = 2, max = 50, message = ResponseMessagesHandler.ValidationMessage.INVALID_USERNAME_SIZE)
    @Pattern(regexp = PatternHandler.USERNAME,
            message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_USERNAME + "}")
    private String username;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_EMAIL)
    @Email(regexp = PatternHandler.EMAIL, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL + "}")
    private String email;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_PASSWORD)
    @Size(min = 8, max = 30, message = ResponseMessagesHandler.ValidationMessage.INVALID_PASSWORD_SIZE)
    @Pattern(regexp = PatternHandler.PASSWORD,
            message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_PASSWORD + "}")
    private String password;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_CONFIRM_PASS)
    private String confirmPassword;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + ResponseMessagesHandler.ValidationMessage.DIFFERENT_PASSWORDS + "}")
    private boolean isPasswordsEquals() {
        return password.equals(confirmPassword);
    }
}
