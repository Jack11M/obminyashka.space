package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageDto {
    private Long id;
    @NotNull(message = "URL has to be not null")
    private String resourceUrl;
    private boolean defaultPhoto;
}
