package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.annotation.FieldMatch;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "newPassword", second = "confirmNewPassword", message = "{different.passwords}")
public class UserChangePasswordDto {

    @NotEmpty(message = "{empty.password}")
    private String oldPassword;

    @NotEmpty(message = "{empty.password}")
    @Size(min = 8, max = 30, message = "{invalid.password.size}")
    @Pattern(regexp = PatternHandler.PASSWORD, message = "{invalid.password}")
    private String newPassword;

    @NotEmpty(message = "{empty.confirm.password}")
    private String confirmNewPassword;
}
