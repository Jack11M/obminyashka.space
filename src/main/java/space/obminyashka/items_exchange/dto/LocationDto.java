package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

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
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String areaUA;

    @ApiModelProperty(required = true)
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String districtUA;

    @ApiModelProperty(required = true)
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String cityUA;

    @ApiModelProperty(required = true)
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String areaEN;

    @ApiModelProperty(required = true)
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String districtEN;

    @ApiModelProperty(required = true)
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 50, message = "{invalid.max-size}")
    private String cityEN;
}
