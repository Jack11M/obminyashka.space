package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
