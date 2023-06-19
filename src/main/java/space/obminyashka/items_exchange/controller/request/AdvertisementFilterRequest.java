package space.obminyashka.items_exchange.controller.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;
import space.obminyashka.items_exchange.model.QAdvertisement;
import space.obminyashka.items_exchange.model.enums.Size;
import space.obminyashka.items_exchange.util.QPredicate;

import java.util.Set;
import java.util.stream.Collectors;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;


@Getter
@Setter
public class AdvertisementFilterRequest {

    @Parameter(description = "Results page you want to retrieve (0..N). Default value: 0")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int page = 0;

    @Parameter(description = "Number of records per page. Default value: 12")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int size = 12;

    @JsonUnwrapped
    @JsonSetter(nulls = Nulls.SKIP)
    private SubcategoryFilter subcategoryFilterRequest = new SubcategoryFilter();

    @JsonUnwrapped
    @JsonSetter(nulls = Nulls.SKIP)
    private AdvertisementFilter advertisementFilter = new AdvertisementFilter();

    @SuppressWarnings("unused")
    @AssertFalse(message = "{" + INVALID_CATEGORY_SIZES_ID + "}")
    private boolean isValidCategorySizes() {
        return CollectionUtils.isNotEmpty(advertisementFilter.getClothingSizes()) &&
                CollectionUtils.isNotEmpty(advertisementFilter.getShoesSizes());
    }

    public Predicate toPredicate() {
        return QPredicate.builder()
                .add(advertisementFilter.getGender(), QAdvertisement.advertisement.gender::eq)
                .add(advertisementFilter.getLocationId(), QAdvertisement.advertisement.location.id::eq)
                .add(advertisementFilter.getSeason(), QAdvertisement.advertisement.season::in)
                .add(extractClothingSizeRanges(advertisementFilter.getClothingSizes()), QAdvertisement.advertisement.size::in)
                .add(extractShoesSizeLengths(advertisementFilter.getShoesSizes()), QAdvertisement.advertisement.size::in)
                .add(advertisementFilter.getAge(), QAdvertisement.advertisement.age::in)
                .add(subcategoryFilterRequest.getSubcategoriesIdValues(), QAdvertisement.advertisement.subcategory.id::in)
                .buildAnd();
    }

    private Set<String> extractClothingSizeRanges(Set<Size.Clothing> clothingSizes) {
        return clothingSizes.stream()
                .map(Size.Clothing::getRange)
                .collect(Collectors.toSet());
    }

    private Set<String> extractShoesSizeLengths(Set<Size.Shoes> shoesSizes) {
        return shoesSizes.stream().map(Size.Shoes::getLength)
                .map(String::valueOf)
                .collect(Collectors.toSet());
    }
}
