package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.annotation.FieldMatch;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import static com.hillel.items_exchange.dto.UserDeleteDto.INVALID_CONFIRM_PASSWORD;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "password", second = "confirmPassword", message = INVALID_CONFIRM_PASSWORD)

public class UserDeleteDto {

    public static final String INVALID_CONFIRM_PASSWORD = "{invalid.confirm.password}";

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String oldPassword;

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String confirmPassword;
}
