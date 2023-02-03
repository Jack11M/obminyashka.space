package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.DealType;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class AdvertisementModificationDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @ApiModelProperty(required = true)
    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY)
    @Size(min = 3, max = 70, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_SIZE + "}")
    private String topic;

    @ApiModelProperty(value = "can't be null, but can be empty")
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 255, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String description;

    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Size(max = 210, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String wishesToExchange;

    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private DealType dealType;

    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private AgeRange age;

    private boolean readyForOffers;

    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private Gender gender;

    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private Season season;

    @Size(min = 1, max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_SIZE + "}")
    @JsonProperty("sizeValue")
    private String size;

    @ApiModelProperty(required = true)
    private long subcategoryId;

    @ApiModelProperty(required = true)
    private UUID locationId;
}
