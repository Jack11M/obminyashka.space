package space.obminyashka.items_exchange.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_SIZE;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class SubcategoryView {

    @JsonProperty
    private long id;

    @Schema(
            description = "name of subcategory",
            type = "String",
            example = "winter shoes",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotEmpty(message = "{" + INVALID_NOT_EMPTY + "}")
    @Size(min = 3, max = 50, message = "{" + INVALID_SIZE + "}")
    private String name;
}
