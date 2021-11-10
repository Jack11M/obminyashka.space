package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChangePasswordDto {

    @NotEmpty(message = "{empty.password}")
    private String oldPassword;

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String newPassword;

    @NotEmpty(message = "{empty.confirm.password}")
    private String confirmNewPassword;

    @SuppressWarnings("unused")                         // Used in validation process by Spring Validator
    @AssertTrue(message = "{different.passwords}")
    private boolean isPasswordsEquals() {
        return newPassword.equals(confirmNewPassword);
    }
}
