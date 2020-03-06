package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    @NotEmpty(message = "{empty.username}")
    @Size(min = 2, max = 50, message = "{invalid.username.size}")
    @Pattern(regexp = "(?=\\S+$).{2,}", message = "{invalid.username}")
    private String username;

    @NotEmpty(message = "{empty.email}")
    @Size(max = 129, message = "{too.big.email}")
    @Email(regexp = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$", message = "{invalid.email}")
    private String email;

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password}")
    @Pattern(regexp = "(?=.*?[0-9])(?=.*?[a-z])(?=.*?[A-Z]).+.{8,}", message = "{invalid.password}")
    private String password;

    @NotEmpty(message = "{empty.confirm.password}")
    private String confirmPassword;
}
