package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;
import lombok.*;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdvertisementFilterDto {
    private Set<@Valid AgeRange> age;
    private Set<@Valid Gender> gender;
    private Set<@Valid Season> season;
    @JsonProperty("sizeValue")
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private Set<String> size;
    private Set<Long> subcategoryId;
    private Set<Long> categoryId;
    @Schema(example = "842f9ab1-95e8-4c81-a49b-fa4f6d0c3a10")
    private UUID locationId;
}
