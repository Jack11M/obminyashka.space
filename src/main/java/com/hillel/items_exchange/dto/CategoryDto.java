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
@ToString
public class CategoryDto {

    @PositiveOrZero(message = "{invalid.id}")
    private long id;

    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 3, max = 50, message = "{invalid.size}")
    private String name;

    @NotNull(message = "{invalid.not-null}")
    private List<@Valid SubcategoryDto> subcategories;
}
