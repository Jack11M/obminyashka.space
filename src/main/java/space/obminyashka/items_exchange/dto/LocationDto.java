package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "{invalid.not-null}")
    @Size(min = 2, max = 100, message = "{invalid.size}")
    private String area;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-null}")
    @Size(min = 2, max = 100, message = "{invalid.size}")
    private String district;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 2, max = 100, message = "{invalid.size}")
    private String city;

    @ApiModelProperty(required = true)
    @NotNull(message = "{invalid.not-null}")
    private String i18N;
}
