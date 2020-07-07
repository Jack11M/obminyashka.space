package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hillel.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserDto {
    @PositiveOrZero(message = "{invalid.id}")
    private long id;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 2, max = 50, message = "{invalid.size}")
    private String username;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(max = 129, message = "{invalid.email}")
    @Email(regexp = PatternHandler.EMAIL, message = "{invalid.email}")
    private String email;
    @NotNull(message = "{invalid.not-null}")
    private Boolean online;
    @NotNull(message = "{invalid.not-null}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{invalid.first-or-last.name}")
    private String firstName;
    @NotNull(message = "{invalid.not-null}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{invalid.first-or-last.name}")
    private String lastName;
    @NotNull(message = "{invalid.not-null}")
    private String avatarImage;
    @NotNull(message = "{invalid.not-null}")
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate lastOnlineTime;
}
