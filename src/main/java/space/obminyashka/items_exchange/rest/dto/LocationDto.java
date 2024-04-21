package space.obminyashka.items_exchange.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE;
import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class LocationDto {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    private String areaUA;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    private String districtUA;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    private String cityUA;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    private String areaEN;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    private String districtEN;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    private String cityEN;
}
