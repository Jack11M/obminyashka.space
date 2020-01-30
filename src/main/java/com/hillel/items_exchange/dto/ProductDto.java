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
@EqualsAndHashCode(exclude = {"subcategory", "images"})
public class ProductDto {
    @PositiveOrZero(message = "Id value has to be 0 or positive")
    private long id;
    @NotEmpty(message = "Age has to be not empty")
    @Size(max = 50, message = "Age has to be less than 50 symbols")
    private String age;
    @NotEmpty(message = "Gender has to be not empty")
    @Size(max = 50, message = "Gender has to be less than 50 symbols")
    private String gender;
    @NotEmpty(message = "Season has to be not empty")
    @Size(max = 50, message = "Season description has to be less than 50 symbols")
    private String season;
    @NotEmpty(message = "Size has to be not empty")
    @Size(max = 50, message = "Size description has to be less than 50 symbols")
    private String size;

    @NotNull(message = "Subcategory has to be not null")
    private @Valid SubcategoryDto subcategory;
    @NotNull(message = "Images has to be not null")
    private List<@Valid ImageDto> images;
}
