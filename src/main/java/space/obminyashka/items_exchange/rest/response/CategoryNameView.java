package space.obminyashka.items_exchange.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryNameView {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "{" + INVALID_NOT_EMPTY + "}")
    @Size(min = 3, max = 50, message = "{" + INVALID_SIZE + "}")
    private String name;
}
