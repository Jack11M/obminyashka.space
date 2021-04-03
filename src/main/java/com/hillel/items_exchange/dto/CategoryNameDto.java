package com.hillel.items_exchange.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.hillel.items_exchange.annotation.Zero;
import com.hillel.items_exchange.mapper.transfer.New;
import io.swagger.annotations.ApiModelProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryNameDto {

    @Zero(groups = New.class, message = "{invalid.new.entity.id}")
    @PositiveOrZero(message = "{invalid.id}")
    private long id;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 3, max = 50, message = "{invalid.size}")
    private String name;
}
