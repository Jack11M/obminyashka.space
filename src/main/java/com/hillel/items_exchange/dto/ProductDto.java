package com.hillel.items_exchange.dto;
import lombok.*;

import javax.validation.Valid;
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
    @NotNull(message = "{invalid.not-null}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String age;
    @NotNull(message = "{invalid.not-null}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String gender;
    @NotNull(message = "{invalid.not-null}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String season;
    @NotNull(message = "{invalid.not-null}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String size;
    @NotNull(message = "{invalid.not-null}")
    private long subcategoryId;
    @NotNull(message = "{invalid.not-null}")
    private List<@Valid ImageDto> images;
}
