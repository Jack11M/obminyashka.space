package com.hillel.items_exchange.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PhoneDto {
    @NotNull(message = "{invalid.not-null}")
    private Boolean show;
    @Pattern(regexp = "^\\s*(?<country>\\+?\\d{1,3})[-. (]*(?<area>\\d{3})[-. )]*(?<number>\\d{3}[-. ]*\\d{2}[-. ]*\\d{2})\\s*$",
            message = "{invalid.phone.number}")
    private String phoneNumber;
    @NotNull(message = "{invalid.not-null}")
    private Boolean defaultPhone;
}
