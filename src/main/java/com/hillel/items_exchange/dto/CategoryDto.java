package com.hillel.items_exchange.dto;

import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

import com.hillel.items_exchange.annotation.Zero;
import com.hillel.items_exchange.mapper.tranfer.Exist;
import com.hillel.items_exchange.mapper.tranfer.New;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {

    @Positive(groups = Exist.class, message = "{invalid.exist.id}")
    @Zero(groups = New.class, message = "{invalid.new.entity.id}")
    private long id;

    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 3, max = 50, message = "{invalid.size}")
    private String name;

    @NotNull(message = "{invalid.not-null}")
    private List<@Valid SubcategoryDto> subcategories;
}
