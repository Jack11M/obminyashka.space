package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.annotation.FieldMatch;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "newPassword", second = "confirmNewPassword",
        message = "{invalid.confirm.password}")
public class UserChangePasswordDto {

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String password;

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String newPassword;

    @NotEmpty(message = "{empty.confirm.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String confirmNewPassword;
}
