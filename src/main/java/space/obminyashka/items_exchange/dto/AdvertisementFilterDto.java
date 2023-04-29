package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;
import lombok.*;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import jakarta.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdvertisementFilterDto {
    private AgeRange age;
    private Gender gender;
    private Season season;
    @JsonProperty("sizeValue")
    @Size(max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE + "}")
    private String size;
    private Long subcategoryId;
    private Long categoryId;
    private UUID locationId;
}
