package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
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

    @ApiModelProperty(required = true)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String areaUA;

    @ApiModelProperty(required = true)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String districtUA;

    @ApiModelProperty(required = true)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String cityUA;

    @ApiModelProperty(required = true)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String areaEN;

    @ApiModelProperty(required = true)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String districtEN;

    @ApiModelProperty(required = true)
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String cityEN;
}
