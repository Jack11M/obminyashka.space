package com.hillel.items_exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class LocationDto {
    @PositiveOrZero(message = "{invalid.id}")
    private long id;
    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 2, max = 100, message = "{invalid.size}")
    private String city;
    @NotNull(message = "{invalid.not-null}")
    private String district;
}
