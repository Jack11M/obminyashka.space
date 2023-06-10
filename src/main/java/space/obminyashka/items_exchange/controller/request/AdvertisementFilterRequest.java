package space.obminyashka.items_exchange.controller.request;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.annotation.Nulls;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.INVALID_NOT_POSITIVE_ID;


@Getter
@Setter
public class AdvertisementFilterRequest {

    @Parameter(name = "page", description = "Results page you want to retrieve (0..N). Default value: 0")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int page = 0;

    @Parameter(name = "size", description = "Number of records per page. Default value: 12")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int size = 12;

    @JsonUnwrapped
    @JsonSetter(nulls = Nulls.SKIP)
    private SubcategorySearch subcategorySearchRequest = new SubcategorySearch();

    @JsonUnwrapped
    @JsonSetter(nulls = Nulls.SKIP)
    private AdvertisementFilter advertisementFilter = new AdvertisementFilter();
}
