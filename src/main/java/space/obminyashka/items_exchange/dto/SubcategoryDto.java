package space.obminyashka.items_exchange.dto;

import space.obminyashka.items_exchange.annotation.Zero;
import space.obminyashka.items_exchange.mapper.transfer.New;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SubcategoryDto {

    @Zero(groups = New.class, message = "{invalid.new.entity.id}")
    @PositiveOrZero(message = "{invalid.id}")
    private long id;

    @ApiModelProperty(
            value = "name of subcategory",
            dataType = "String",
            example = "winter shoes",
            required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 3, max = 50, message = "{invalid.size}")
    private String name;
}
