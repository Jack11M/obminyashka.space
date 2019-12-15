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
    @PositiveOrZero(message = "Id value has to be 0 or positive")
    private Long id;
    @NotNull(message = "URL has to be not null")
    private String resourceUrl;
    private boolean defaultPhoto;
}
