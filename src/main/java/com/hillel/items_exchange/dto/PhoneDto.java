package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.util.PatternHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class PhoneDto {

    @PositiveOrZero(message = "{invalid.id}")
    private long id;

    @ApiModelProperty(required = true, example = "+38(123)456-78-90, 38 123 456 78 90")
    @Pattern(regexp = PatternHandler.PHONE_NUMBER, message = "{invalid.phone.number}")
    private String phoneNumber;

    @NotNull(message = "{invalid.not-null}")
    private boolean defaultPhone;
}