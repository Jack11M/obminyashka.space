package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.util.PatternHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PhoneDto {
    @PositiveOrZero(message = "{invalid.id}")
    private long id;
    @NotNull(message = "{invalid.not-null}")
    private Boolean show;
    @Pattern(regexp = PatternHandler.PHONE_NUMBER, message = "{invalid.phone.number}")
    private String phoneNumber;
    @NotNull(message = "{invalid.not-null}")
    private Boolean defaultPhone;
}