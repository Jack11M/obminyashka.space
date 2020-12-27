package com.hillel.items_exchange.dto;

import java.util.List;

import com.hillel.items_exchange.model.enums.AgeRange;
import com.hillel.items_exchange.model.enums.DealType;
import com.hillel.items_exchange.model.enums.Gender;
import com.hillel.items_exchange.model.enums.Season;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"id", "defaultPhoto", "location", "images"})
public class AdvertisementDto {
    @PositiveOrZero(message = "{invalid.id}")
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

    private MultipartFile defaultPhoto;

    @NotNull(message = "{invalid.not-null}")
    @Size(min = 1, max = 50, message = "{invalid.size}")
    private String size;

    @NotNull(message = "{invalid.not-null}")
    private long subcategoryId;
    @NotNull(message = "{invalid.not-null}")
    private @Valid LocationDto location;

    @NotNull(message = "{invalid.not-null}")
    private List<@Valid ImageDto> images;
}