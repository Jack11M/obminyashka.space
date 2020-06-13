package com.hillel.items_exchange.dto;

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
    @Pattern(regexp = "^\\s*(?<country>\\+?\\d{1,3})[-. (]*(?<area>\\d{3})[-. )]*(?<number>\\d{3}[-. ]*\\d{2}[-. ]*\\d{2})\\s*$",
            message = "{invalid.phone.number}")
    private String phoneNumber;
    @NotNull(message = "{invalid.not-null}")
    private Boolean defaultPhone;
}