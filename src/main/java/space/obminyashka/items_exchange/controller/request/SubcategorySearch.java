package space.obminyashka.items_exchange.controller.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SubcategorySearch {

    @Positive
    private long categoryId;
    private List<@Positive Long> subcategoriesIdValues = new ArrayList<>();
}
