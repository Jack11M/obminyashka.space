package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hillel.items_exchange.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {
    @PositiveOrZero(message = "{invalid.id}")
    private long id;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 2, max = 50, message = "{invalid.size}")
    private String username;
    @NotEmpty(message = "{invalid.not-empty}")
    private String password;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(max = 129, message = "{invalid.email}")
    @Email(regexp = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$", message = "{invalid.email}")
    private String email;
    @NotNull(message = "{invalid.not-null}")
    private Boolean online;
    @NotNull(message = "{invalid.not-null}")
    @Size(min = 2, max = 50, message = "{invalid.size}")
    private String firstName;
    @NotNull(message = "{invalid.not-null}")
    @Size(min = 2, max = 50, message = "{invalid.size}")
    private String lastName;
    @NotNull(message = "{invalid.not-null}")
    private String avatarImage;
    @NotNull(message = "{invalid.not-null}")
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime lastOnlineTime;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.avatarImage = user.getAvatarImage();
        this.email = user.getEmail();
        this.lastOnlineTime = user.getLastOnlineTime();
        this.online = user.getOnline();
        this.password = user.getPassword();

    }
}
