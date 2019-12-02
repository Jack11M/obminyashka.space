package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Boolean online;
    private String firstName;
    private String lastName;
    private String avatarImage;
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate lastOnlineTime;

}
