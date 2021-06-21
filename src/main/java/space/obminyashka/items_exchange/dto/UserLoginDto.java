package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @Size(max = 130, message = "{invalid.max-size}")
    @NotEmpty(message = "{empty.username}")
    private String usernameOrEmail;

    @NotEmpty(message = "{empty.password}")
    private String password;
}
