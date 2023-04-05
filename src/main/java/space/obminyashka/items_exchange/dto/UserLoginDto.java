package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @Size(max = 130, message = ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE)
    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_USERNAME)
    private String usernameOrEmail;

    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.EMPTY_PASSWORD)
    private String password;
}
