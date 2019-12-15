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
public class CategoryDto {
    @PositiveOrZero(message = "Id value has to be 0 or positive")
    private Long id;
    @NotEmpty(message = "Category name has to be not empty")
    @Size(min = 3, max = 50, message = "Category name has to be between 3 and 50 symbols")
    private String name;
}
