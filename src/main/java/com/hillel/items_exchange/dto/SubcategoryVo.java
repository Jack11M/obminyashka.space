package com.hillel.items_exchange.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SubcategoryVo {
    @PositiveOrZero(message = "Subcategory id has to be greater than 0 if it exists or 0 if it is new")
    private long id;
    @NotEmpty(message = "Subcategory name has to be not empty")
    @Size(min = 3, max = 50, message = "Subcategory name has to be between 3 and 50 symbols")
    private String name;
}
