package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    @NotEmpty(message = "Login field is required")
    @Size(min = 2, max = 50, message = "Your login must be between 2 and 50 characters")
    private String username;
    @NotEmpty(message = "Password field is required")
    private String password;
    @NotEmpty(message = "Email field is required")
    @Size(max = 129, message = "Email length should be less than 129 characters. Please enter valid email address")
    @Email(message = "Please enter valid email address (Ex: username@example.com)")
    private String email;
    @NotNull(message = "Online status has to be not null")
    private Boolean online;
    @NotNull(message = "First name has to be not empty")
    @Size(min = 2, max = 50, message = "First name has to be between 2 and 50 symbols")
    private String firstName;
    @NotNull(message = "Last name has to be not empty")
    @Size(min = 2, max = 50, message = "Last name has to be between 2 and 50 symbols")
    private String lastName;
    @NotNull(message = "Avatar image has to be not empty")
    private String avatarImage;
    @NotNull(message = "Last online date has to be not null")
    @PastOrPresent(message = "Last online date couldn't be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastOnlineTime;

}
