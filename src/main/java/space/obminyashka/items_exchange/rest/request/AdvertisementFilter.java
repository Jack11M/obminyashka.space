package space.obminyashka.items_exchange.rest.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.repository.enums.AgeRange;
import space.obminyashka.items_exchange.repository.enums.Gender;
import space.obminyashka.items_exchange.repository.enums.Season;
import space.obminyashka.items_exchange.repository.enums.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class AdvertisementFilter {

    @Parameter(name = "excludeAdvertisementId", description = "ID of excluded advertisement", allowEmptyValue = true)
    private UUID excludeAdvertisementId;

    @Parameter(description = "Location ID for advertisements filtering", example = "842f9ab1-95e8-4c81-a49b-fa4f6d0c3a10",
            allowEmptyValue = true)
    private UUID locationId;

    @Parameter(description = "Gender of a child for advertisements filtering", allowEmptyValue = true)
    private Gender gender;

    @Parameter(description = "Multiple ages of a child for advertisements filtering", allowEmptyValue = true)
    @JsonSetter(nulls = Nulls.SKIP)
    private Set<AgeRange> age = new HashSet<>();

    @JsonSetter(nulls = Nulls.SKIP)
    @Parameter(description = "Multiple clothes sizes for advertisements filtering. Applicable ONLY when the Category ID is 1 (clothing)", allowEmptyValue = true)
    private Set<Size.Clothing> clothingSizes = new HashSet<>();

    @JsonSetter(nulls = Nulls.SKIP)
    @Parameter(description = "Multiple shoe sizes for advertisements filtering. Applicable ONLY when the Category ID is 2 (shoes)", allowEmptyValue = true)
    private Set<Size.Shoes> shoesSizes = new HashSet<>();

    @Parameter(description = "Clothing or Shoes season for advertisements filtering", allowEmptyValue = true)
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

    public void setAge(Set<String> value) {
        age = value.stream().map(AgeRange::fromValue).collect(Collectors.toSet());
    }
}
