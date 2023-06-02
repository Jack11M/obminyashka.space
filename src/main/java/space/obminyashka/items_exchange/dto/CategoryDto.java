package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CategoryDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @Schema(
            description = "name of category",
            type = "String",
            example = "shoes",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "{" + INVALID_NOT_EMPTY + "}")
    @Size(min = 3, max = 50, message = "{" + INVALID_SIZE + "}")
    private String name;
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private List<@Valid SubcategoryDto> subcategories;
}
