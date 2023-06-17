package space.obminyashka.items_exchange.controller.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubcategorySearch {

    @Parameter(description = "Id of necessary category(1 - Clothing; 2 - Shoes etc.)", example = "1")
    @Positive
    private long categoryId;

    @Parameter(description = "Ids of necessary subcategory")
    private List<@Positive Long> subcategoriesIdValues = new ArrayList<>();
}
