package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLoginDto {

    @NotEmpty(message = "{empty.username}")
    private String usernameOrEmail;

    @NotEmpty(message = "{empty.password}")
    private String password;
}
