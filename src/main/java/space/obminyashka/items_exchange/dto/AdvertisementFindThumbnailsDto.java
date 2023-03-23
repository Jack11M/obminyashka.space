package space.obminyashka.items_exchange.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.UUID;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.INVALID_NOT_POSITIVE_ID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementFindThumbnailsDto {

    @Parameter(name = "page", description = "Results page you want to retrieve (0..N). Default value: 0")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int page = 0;


    @Parameter(name = "size", description = "Number of records per page. Default value: 12")
    @PositiveOrZero(message = "{" + INVALID_NOT_POSITIVE_ID + "}")
    private int size = 12;

    @Parameter(name = "subcategoryId", description = "ID of existed subcategory for searching same advertisements")
    private Long subcategoryId;

    @Parameter(name = "excludeAdvertisementId", description = "ID of excluded advertisement")
    private UUID excludeAdvertisementId;

}
