package space.obminyashka.items_exchange.controller.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;
import space.obminyashka.items_exchange.model.QAdvertisement;
import space.obminyashka.items_exchange.util.QPredicate;

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
    private SubcategorySearch subcategorySearchRequest = new SubcategorySearch();

    @JsonUnwrapped
    @JsonSetter(nulls = Nulls.SKIP)
    private AdvertisementFilter advertisementFilter = new AdvertisementFilter();

    public Predicate toPredicate() {
        Predicate predicate = QPredicate.builder()
                .add(advertisementFilter.getGender(), QAdvertisement.advertisement.gender::eq)
                .add(advertisementFilter.getLocationId(), QAdvertisement.advertisement.location.id::eq)
                .add(advertisementFilter.getSeason(), QAdvertisement.advertisement.season::in)
                .add(advertisementFilter.getClothingSizes().stream().map(x -> x.range).collect(Collectors.toSet()),
                        QAdvertisement.advertisement.size::in)
                .add(advertisementFilter.getShoesSizes().stream().map(x -> String.valueOf(x.length)).collect(Collectors.toSet()),
                        QAdvertisement.advertisement.size::in)
                .add(advertisementFilter.getAge(), QAdvertisement.advertisement.age::in)
                .add(subcategorySearchRequest.getSubcategoriesIdValues(), QAdvertisement.advertisement.subcategory.id::in)
                .buildAnd();
        return predicate == null ? Expressions.asBoolean(true).isTrue() : predicate;
    }

}
