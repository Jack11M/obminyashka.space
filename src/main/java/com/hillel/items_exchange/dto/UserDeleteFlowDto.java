package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.annotation.FieldMatch;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "password", second = "confirmPassword", message = "{different.passwords}")
public class UserDeleteFlowDto {

    @NotEmpty(message = "{empty.password}")
    private String password;

    @NotEmpty(message = "{empty.password}")
    private String confirmPassword;
}
