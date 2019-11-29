package com.hillel.items_exchange.dto;

import com.hillel.items_exchange.model.DealType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementDto {

    private Long id;
    @NotEmpty
    @Size(min = 3, max = 50, message = "Topic must be between 3 and 50 symbols")
    private String topic;
    @NotEmpty
    @Size(max = 512, message = "Description must be less than 512 symbols")
    private String description;
    @NotEmpty
    @Size
    private String wishesToExchange;
    @NotNull
    private Boolean readyForOffers;
    @NotNull
    private Boolean isFavourite;
    @NotNull
    private DealType dealType;
    @NotNull
    @Valid
    private LocationDto location;
    @NotNull
    @Valid
    private ProductDto product;
    @NotNull
    @Valid
    private UserDto user;

}
