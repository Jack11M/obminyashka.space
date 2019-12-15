package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LocationDto {
    @PositiveOrZero(message = "Id value has to be 0 or positive")
    private Long id;
    @NotEmpty(message = "City name has to be not empty")
    @Size(min = 2, max = 100, message = "City name has to be between 2 and 100 symbols")
    private String city;
    @NotNull(message = "District has to be not null")
    private String district;
}
