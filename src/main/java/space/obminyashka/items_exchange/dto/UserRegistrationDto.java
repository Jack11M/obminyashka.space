package space.obminyashka.items_exchange.dto;

import space.obminyashka.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    @NotEmpty(message = "{empty.username}")
    @Size(min = 2, max = 50, message = "{invalid.username.size}")
    @Pattern(regexp = PatternHandler.USERNAME, message = "{invalid.username}")
    private String username;

    @NotEmpty(message = "{empty.email}")
    @Email(message = "{invalid.email}")
    private String email;

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String password;

    @NotEmpty(message = "{empty.confirm.password}")
    private String confirmPassword;

    @AssertTrue(message = "{different.passwords}")
    private boolean isPasswordsEquals() {
        return password.equals(confirmPassword);
    }
}
