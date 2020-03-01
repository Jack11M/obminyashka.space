package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginDto {

    @NotEmpty(message = "{empty.username}")
    private String usernameOrEmail;

    @NotEmpty(message = "{empty.password}")
    private String password;
}
