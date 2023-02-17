package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

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
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String areaUA;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String districtUA;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String cityUA;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String areaEN;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String districtEN;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String cityEN;
}
