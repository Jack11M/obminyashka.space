package space.obminyashka.items_exchange.controller.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.INVALID_MAX_SIZE;

@Getter
@Setter
public class AdvertisementFilter {

    @Parameter(name = "locationId", description = "Location of clothes/shoes", allowEmptyValue = true, example = "842f9ab1-95e8-4c81-a49b-fa4f6d0c3a10")
    private UUID locationId;

    @Parameter(name = "gender", description = "Gender of child", allowEmptyValue = true)
    private Gender gender;

    @Parameter(name = "age", description = "Age of child", allowEmptyValue = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<AgeRange> age = new HashSet<>();

    @JsonSetter(nulls = Nulls.SKIP)
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    @Parameter(description = "Size of clothes", allowEmptyValue = true)
    private Set<space.obminyashka.items_exchange.model.enums.Size.Clothing> clothingSizes = new HashSet<>();

    @JsonSetter(nulls = Nulls.SKIP)
    @Size(max = 50, message = "{" + INVALID_MAX_SIZE + "}")
    @Parameter(description = "Size of shoes", allowEmptyValue = true)
    private Set<space.obminyashka.items_exchange.model.enums.Size.Shoes> shoesSizes = new HashSet<>();

    @Parameter(name = "season", description = "Season of clothes/shoes", allowEmptyValue = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<Season> season = new HashSet<>();
}
