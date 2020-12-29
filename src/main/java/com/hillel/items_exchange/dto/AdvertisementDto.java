package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.annotation.Zero;
import com.hillel.items_exchange.mapper.transfer.Exist;
import com.hillel.items_exchange.mapper.transfer.New;
import com.hillel.items_exchange.model.enums.AgeRange;
import com.hillel.items_exchange.model.enums.DealType;
import com.hillel.items_exchange.model.enums.Gender;
import com.hillel.items_exchange.model.enums.Season;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id", "location", "images"})
public class AdvertisementDto {
    @Positive(groups = Exist.class, message = "{invalid.exist.id}")
    @Zero(groups = New.class, message = "{new.advertisement.id.not-zero}")
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
    private boolean readyForOffers;

    @NotNull(message = "{invalid.not-null}")
    private DealType dealType;

    @NotNull(message = "{invalid.not-null}")
    private AgeRange age;

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
    private @Valid LocationDto location;

    private List<@Valid ImageDto> images;
}