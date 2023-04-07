package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDeleteFlowDto {

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_PASSWORD)
    private String password;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_PASSWORD)
    private String confirmPassword;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + ResponseMessagesHandler.ValidationMessage.DIFFERENT_PASSWORDS + "}")
    private boolean isPasswordsEquals() {
        return password.equals(confirmPassword);
    }
}
