package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.util.PatternHandler;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeEmailDto {

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY)
    @Email(regexp = PatternHandler.EMAIL, message = ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL)
    private String email;
}
