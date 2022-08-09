package space.obminyashka.items_exchange.dto;

import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;
import lombok.*;

import javax.validation.constraints.Size;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AdvertisementFilterDto {
    private AgeRange age;
    private Gender gender;
    private Season season;
    @Size(max = 50, message = "{invalid.max-size}")
    private String size;
    private Long subcategoryId;
    private Long categoryId;
    private UUID locationId;
}
