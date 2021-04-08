package space.obminyashka.items_exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

import space.obminyashka.items_exchange.annotation.Zero;
import space.obminyashka.items_exchange.mapper.transfer.Exist;
import space.obminyashka.items_exchange.mapper.transfer.New;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {

    @Positive(groups = Exist.class, message = "{invalid.exist.id}")
    @Zero(groups = New.class, message = "{invalid.new.entity.id}")
    private long id;

    @ApiModelProperty(
            value = "name of category",
            dataType = "String",
            example = "shoes",
            required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 3, max = 50, message = "{invalid.size}")
    private String name;
    @NotNull(message = "{invalid.not-null}")
    private List<@Valid SubcategoryDto> subcategories;
}
