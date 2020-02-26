package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AdvertisementFilterDto {
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String age;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String gender;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String season;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String size;
    @PositiveOrZero(message = "{invalid.id}")
    private long subcategoryId;
    @PositiveOrZero(message = "{invalid.id}")
    private long categoryId;
    @PositiveOrZero(message = "{invalid.id}")
    private long locationId;
}
