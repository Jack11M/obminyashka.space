package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.annotation.FieldMatch;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldMatch(first = "newEmail", second = "newEmailConfirmation",
        message = "{invalid.confirm.email}")
public class UserChangeEmailDto {

    @NotEmpty(message = "{invalid.not-empty}")
    @Email(regexp = PatternHandler.EMAIL, message = "{invalid.email}")
    private String newEmail;

    @NotEmpty(message = "{invalid.not-empty}")
    @Email(regexp = PatternHandler.EMAIL, message = "{invalid.email}")
    private String newEmailConfirmation;
}
