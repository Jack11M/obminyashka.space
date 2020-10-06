package com.hillel.items_exchange.dto;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String age;
    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String gender;
    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String season;
    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String size;
    @NotNull(message = "{invalid.not-null}")
    private long subcategoryId;
    @NotNull(message = "{invalid.not-null}")
    private List<@Valid ImageDto> images;
}
