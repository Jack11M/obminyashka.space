package space.obminyashka.items_exchange.controller.request;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class SubcategorySearch {

    @Positive
    private long categoryId;
    private Set<@Positive Long> subcategoriesIdValues = new HashSet<>();
}
