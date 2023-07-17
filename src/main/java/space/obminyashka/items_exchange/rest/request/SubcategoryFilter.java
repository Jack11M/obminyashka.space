package space.obminyashka.items_exchange.rest.request;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubcategoryFilter {

    @Parameter(description = "Category ID for advertisements filtering. See the full list of Categories in: /api/v1/category/all", example = "1")
    @Positive
    private long categoryId;

    @Parameter(description = "Subcategories ID for advertisements filtering. Should belong to passed Category ID")
    private List<@Positive Long> subcategoriesIdValues = new ArrayList<>();
}
