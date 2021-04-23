package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserChangeEmailDto {

    @NotEmpty(message = "{invalid.not-empty}")
    @Email(message = "{invalid.email}")
    private String newEmail;

    @NotEmpty(message = "{invalid.not-empty}")
    private String newEmailConfirmation;

    @AssertTrue(message = "{invalid.confirm.email}")
    private boolean isEmailEquals() {
        return newEmail.equals(newEmailConfirmation);
    }
}
