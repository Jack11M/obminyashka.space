package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;
import lombok.*;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdvertisementFilterDto {
    private List<AgeRange> age;
    private List<Gender> gender;
    private List<Season> season;
    @JsonProperty("sizeValue")
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private List<String> size;
    private List<Long> subcategoryId;
    private List<Long> categoryId;
    private UUID locationId;
}
