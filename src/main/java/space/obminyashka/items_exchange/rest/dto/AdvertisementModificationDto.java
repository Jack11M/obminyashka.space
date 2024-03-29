package space.obminyashka.items_exchange.rest.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import space.obminyashka.items_exchange.repository.enums.AgeRange;
import space.obminyashka.items_exchange.repository.enums.DealType;
import space.obminyashka.items_exchange.repository.enums.Gender;
import space.obminyashka.items_exchange.repository.enums.Season;

import java.util.UUID;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class AdvertisementModificationDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID id;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "{" + BLANK_TOPIC + "}")
    @Size(min = 3, max = 70, message = "{" + INVALID_SIZE + "}")
    private String topic;

    @Schema(description = "can't be null, but can be empty")
    @NotBlank(message = "{" + BLANK_DESCRIPTION + "}")
    @Size(max = 255, message = "{" + INVALID_MAX_SIZE + "}")
    private String description;

    @Size(max = 210, message = "{" + INVALID_MAX_SIZE + "}")
    private String wishesToExchange;

    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private DealType dealType;

    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private AgeRange age;

    private boolean readyForOffers;

    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private Gender gender;

    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private Season season;

    @Size(min = 1, max = 50, message = "{" + INVALID_SIZE + "}")
    @JsonProperty("sizeValue")
    private String size;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private long subcategoryId;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    private UUID locationId;

    @AssertTrue(message = "{" + BLANK_WISHES_TO_EXCHANGE + "}")
    private boolean isWishesToExchangeNotBlankInComparisonWithReadyForOffers() {
        return (readyForOffers && wishesToExchange.isBlank()) || !wishesToExchange.isBlank();
    }
}
