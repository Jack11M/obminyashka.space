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
    @NotEmpty(message = "Username has to be not empty")
    @Size(min = 3, max = 50, message = "Username has to be between 3 and 50 symbols")
    private String username;
    @NotEmpty(message = "Password must be not empty")
    private String password;
    @Email(message = "E-Mail has to be valid")
    private String email;
    @NotNull(message = "Online status has to be not null")
    private Boolean online;
    @NotEmpty(message = "First name has to be not empty")
    @Size(min = 2, max = 50, message = "First name has to be between 2 and 50 symbols")
    private String firstName;
    @NotEmpty(message = "Last name has to be not empty")
    @Size(min = 2, max = 50, message = "Last name has to be between 2 and 50 symbols")
    private String lastName;
    @NotEmpty(message = "Avatar image has to be not empty")
    private String avatarImage;
    @NotNull(message = "Last online date has to be not null")
    @PastOrPresent(message = "Last online date couldn't be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastOnlineTime;

}
