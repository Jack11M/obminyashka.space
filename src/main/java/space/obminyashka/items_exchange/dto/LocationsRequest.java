package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LocationsRequest {
    @JsonProperty("devices")
    public List<RawLocation> rawLocations;
}
