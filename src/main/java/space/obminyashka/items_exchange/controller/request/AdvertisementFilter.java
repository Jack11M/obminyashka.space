package space.obminyashka.items_exchange.controller.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;
import space.obminyashka.items_exchange.model.enums.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@Getter
@Setter
public class AdvertisementFilter {

    @Parameter(description = "Location of necessary staff", example = "842f9ab1-95e8-4c81-a49b-fa4f6d0c3a10",
            allowEmptyValue = true)
    private UUID locationId;

    @Parameter(description = "Gender of child", allowEmptyValue = true)
    private Gender gender;

    @Parameter(description = "Age of child", allowEmptyValue = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<AgeRange> age = new HashSet<>();

    @JsonSetter(nulls = Nulls.SKIP)
    @Max(value = 50, message = "{" + INVALID_MAX_SIZE + "}")
    @Parameter(description = "Size of clothes(only for category id = 1))", allowEmptyValue = true)
    private Set<Size.Clothing> clothingSizes = new HashSet<>();

    @JsonSetter(nulls = Nulls.SKIP)
    @Max(value = 50, message = "{" + INVALID_MAX_SIZE + "}")
    @Parameter(description = "Size of shoes (only for category id = 2)", allowEmptyValue = true)
    private Set<Size.Shoes> shoesSizes = new HashSet<>();

    @Parameter(description = "Season of clothes/shoes", allowEmptyValue = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<Season> season = new HashSet<>();

    @JsonCreator
    public void setShoesSizes(Set<Double> value) {
        shoesSizes = value.stream().map(Size.Shoes::fromValue).collect(Collectors.toSet());
    }

    @JsonCreator
    public void setClothingSizes(Set<String> value) {
        clothingSizes = value.stream().map(Size.Clothing::fromValue).collect(Collectors.toSet());
    }

    @JsonCreator
    public void setAge(Set<String> value) {
        age = value.stream().map(AgeRange::fromValue).collect(Collectors.toSet());
    }
}
