package com.hillel.evoApp.dto;

import com.hillel.evoApp.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdvertisementDto {

    @NotEmpty
    @Size(min = 3, max = 50, message = "Topic must be between 3 and 50 symbols")
    private String topic;
    @NotNull
    private DealType dealType;
    @NotNull
    private Boolean isFavourite;
    @NotEmpty
    @Size(max = 512, message = "Description must be less than 512 symbols")
    private String description;
    @NotNull
    private Location locations;
    @NotNull
    private List<AdvertisementImage> advertisementImages;
    @NotNull
    private List<ExchangeProduct> exchangeProducts;
    @NotNull
    private Product product;
    @NotNull
    private User user;
}
