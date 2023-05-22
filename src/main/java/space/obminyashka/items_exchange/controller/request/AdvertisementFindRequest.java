package space.obminyashka.items_exchange.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;
import org.springframework.lang.Nullable;

import jakarta.validation.constraints.PositiveOrZero;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@Getter
@Setter
public class AdvertisementFindRequest {

    @Parameter(name = "page", description = "Results page you want to retrieve (0..N). Default value: 0")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int page = 0;


    @Parameter(name = "size", description = "Number of records per page. Default value: 12")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int size = 12;

    @Parameter(name = "subcategoryId", description = "ID of existed subcategory for searching same advertisements")
    @Nullable
    private Long subcategoryId;

    @Parameter(name = "excludeAdvertisementId", description = "ID of excluded advertisement")
    @Nullable
    private UUID excludeAdvertisementId;

    private boolean enableRandom = true;

}
