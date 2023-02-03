package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordDto {

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_PASSWORD)
    private String oldPassword;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_PASSWORD)
    @Size(min = 8, max = 30, message = ResponseMessagesHandler.ValidationMessage.INVALID_PASSWORD_SIZE)
    @Pattern(regexp = PatternHandler.PASSWORD, message = ResponseMessagesHandler.ValidationMessage.INVALID_PASSWORD)
    private String newPassword;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_CONFIRM_PASS)
    private String confirmNewPassword;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + ResponseMessagesHandler.ValidationMessage.DIFFERENT_PASSWORDS + "}")
    private boolean isPasswordsEquals() {
        return newPassword.equals(confirmNewPassword);
    }

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + ResponseMessagesHandler.ValidationMessage.SAME_PASSWORDS + "}")
    private boolean isNewPasswordDifferent() {
        return !newPassword.equals(oldPassword);
    }
}
