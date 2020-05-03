package com.hillel.items_exchange.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"images"})
public class ProductDto {
    @PositiveOrZero(message = "{invalid.id}")
    private long id;
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
    @NotNull(message = "{invalid.not-null}")
    private long subcategoryId;
    @NotNull(message = "{invalid.not-null}")
    private List<@Valid ImageDto> images;
}
