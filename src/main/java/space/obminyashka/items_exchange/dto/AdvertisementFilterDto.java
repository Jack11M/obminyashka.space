package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@Getter
@Setter
public class AdvertisementFilterDto {

    @Parameter(name = "page", description = "Results page you want to retrieve (0..N). Default value: 0")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int page = 0;

    @Parameter(name = "size", description = "Number of records per page. Default value: 12")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int size = 12;

    @Parameter(name = "age", description = "Age of child", allowEmptyValue = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<AgeRange> age = new HashSet<>();

    @Parameter(name = "gender", description = "Gender of child", allowEmptyValue = true)
    private Gender gender;

    @Parameter(name = "season", description = "Season of clothes/shoes", allowEmptyValue = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<Season> season = new HashSet<>();

    @JsonProperty("sizeClothesValue")
    @JsonSetter(nulls = Nulls.SKIP)
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    @Parameter(name = "sizeClothes", description = "Size of clothes", allowEmptyValue = true)
    private Set<space.obminyashka.items_exchange.model.enums.Size.Clothing> sizeClothes = new HashSet<>();

    @JsonProperty("sizeShoesValue")
    @JsonSetter(nulls = Nulls.SKIP)
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    @Parameter(name = "sizeShoes", description = "Size of shoes", allowEmptyValue = true)
    private Set<space.obminyashka.items_exchange.model.enums.Size.Shoes> sizeShoes = new HashSet<>();

    @Parameter(name = "subcategoryId", description = "Subcategory of clothes/shoes", allowEmptyValue = true, example = "1")
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<Long> subcategoryId = new HashSet<>();

    @Parameter(name = "locationId", description = "Location of clothes/shoes", allowEmptyValue = true, example = "842f9ab1-95e8-4c81-a49b-fa4f6d0c3a10")
    private UUID locationId;

    @AssertTrue(message = "{" + INVALID_SIZE_SUBCATEGORY_COMBINATION + "}")
    private boolean isValidSizeAndSubcategory() {
        if (subcategoryId.stream().anyMatch(id -> id >= 1 && id <= 12)) {
            return sizeShoes.isEmpty();
        } else if (subcategoryId.stream().anyMatch(id -> id >= 13 && id <= 17)) {
            return sizeClothes.isEmpty();
        } else {
            return sizeClothes.isEmpty() && sizeShoes.isEmpty();
        }
    }
}
