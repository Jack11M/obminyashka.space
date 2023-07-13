package space.obminyashka.items_exchange.rest.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequest {

    @Size(max = 130, message = "{" + INVALID_MAX_SIZE + "}")
    @NotEmpty(message = "{" + EMPTY_USERNAME + "}")
    private String usernameOrEmail;

    @NotEmpty(message = "{" + EMPTY_PASSWORD + "}")
    private String password;
}
