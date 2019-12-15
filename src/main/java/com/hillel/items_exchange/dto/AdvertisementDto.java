package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.model.DealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementDto {
    @PositiveOrZero(message = "Id value has to be 0 or positive")
    private Long id;
    @NotEmpty(message = "Topic has to be not empty")
    @Size(min = 3, max = 70, message = "Topic must be between 3 and 70 symbols")
    private String topic;
    @NotEmpty(message = "Description has to be not empty")
    @Size(max = 255, message = "Description must be less than 255 symbols")
    private String description;
    @NotNull(message = "Wishes to exchange have to be not null")
    @Size(max = 210, message = "Wishes to exchange have to be less than 210 symbols")
    private String wishesToExchange;
    @NotNull(message = "Ready for offers parameter has to be not null")
    private Boolean readyForOffers;
    @NotNull(message = "Is favourite parameter has to be not null")
    private Boolean isFavourite;
    @NotNull(message = "Deal type has to be not null")
    private DealType dealType;
    @NotNull(message = "Location has to be not null")
    private @Valid LocationDto location;
    @NotNull(message = "Product has to be not null")
    private @Valid ProductDto product;

}
