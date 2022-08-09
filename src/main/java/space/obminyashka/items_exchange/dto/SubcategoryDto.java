package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SubcategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
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
