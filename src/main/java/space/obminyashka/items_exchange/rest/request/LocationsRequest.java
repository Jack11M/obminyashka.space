package space.obminyashka.items_exchange.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LocationsRequest {
    @JsonProperty("devices")
    public List<RawLocation> rawLocations;
}
