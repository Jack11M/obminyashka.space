package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeEmailDto {

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY)
    @Email(message = ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL)
    private String newEmail;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY)
    private String newEmailConfirmation;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_CONFIRM_EMAIL + "}")
    private boolean isEmailEquals() {
        return newEmail.equals(newEmailConfirmation);
    }
}
