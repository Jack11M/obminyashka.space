package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserDeleteFlowDto {

    @NotEmpty(message = "{empty.password}")
    private String password;

    @NotEmpty(message = "{empty.password}")
    private String confirmPassword;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{different.passwords}")
    private boolean isPasswordsEquals() {
        return password.equals(confirmPassword);
    }
}
