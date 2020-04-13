package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile resource;
    private boolean defaultPhoto;
}
