package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageDto {
    @PositiveOrZero(message = "{invalid.id}")
    private long id;
    @NotNull(message = "{invalid.not-null}")
    private String resourceUrl;
    private boolean defaultPhoto;
}
