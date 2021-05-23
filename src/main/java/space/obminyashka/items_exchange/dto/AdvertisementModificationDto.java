package space.obminyashka.items_exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import space.obminyashka.items_exchange.annotation.Zero;
import space.obminyashka.items_exchange.mapper.transfer.Exist;
import space.obminyashka.items_exchange.mapper.transfer.New;
import space.obminyashka.items_exchange.model.enums.AgeRange;
import space.obminyashka.items_exchange.model.enums.DealType;
import space.obminyashka.items_exchange.model.enums.Gender;
import space.obminyashka.items_exchange.model.enums.Season;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class AdvertisementModificationDto {

    @Positive(groups = Exist.class, message = "{invalid.exist.id}")
    @Zero(groups = New.class, message = "{new.advertisement.id.not-zero}")
    @NotNull(message = "{invalid.not-null}")
    private long id;

    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 3, max = 70, message = "{invalid.size}")
    private String topic;

    @ApiModelProperty(value = "can't be null, but can be empty")
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 255, message = "{invalid.max-size}")
    private String description;

    @NotNull(message = "{invalid.not-null}")
    @Size(max = 210, message = "{invalid.max-size}")
    private String wishesToExchange;

    @NotNull(message = "{invalid.not-null}")
    private DealType dealType;

    @NotNull(message = "{invalid.not-null}")
    private AgeRange age;

    @NotNull(message = "{invalid.not-null}")
    private boolean readyForOffers;

    @NotNull(message = "{invalid.not-null}")
    private Gender gender;

    @NotNull(message = "{invalid.not-null}")
    private Season season;

    @NotNull(message = "{invalid.not-null}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String size;

    @NotNull(message = "{invalid.not-null}")
    private long subcategoryId;

    @NotNull(message = "{invalid.not-null}")
    private long locationId;
}
