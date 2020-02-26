package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.model.DealType;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdvertisementDto {
    @PositiveOrZero(message = "{invalid.id}")
    private long id;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 3, max = 70, message = "{invalid.size}")
    private String topic;
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(max = 255, message = "{invalid.max-size}")
    private String description;
    @NotNull(message = "{invalid.not-null}")
    @Size(max = 210, message = "{invalid.max-size}")
    private String wishesToExchange;
    @NotNull(message = "{invalid.not-null}")
    private Boolean readyForOffers;
    @NotNull(message = "{invalid.not-null}")
    private Boolean isFavourite;
    @NotNull(message = "{invalid.not-null}")
    private DealType dealType;
    @NotNull(message = "{invalid.not-null}")
    private @Valid LocationDto location;
    @NotNull(message = "{invalid.not-null}")
    private @Valid ProductDto product;

}
