package com.hillel.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SubcategoryDto {
    @PositiveOrZero(message = "Id value has to be 0 or positive")
    private long id;
    @NotEmpty(message = "Subcategory name has to be not empty")
    @Size(min = 3, max = 50, message = "Subcategory name has to be between 3 and 50 symbols")
    private String name;
    @NotNull(message = "Category has to be not null")
    private @Valid CategoryDto category;
}
