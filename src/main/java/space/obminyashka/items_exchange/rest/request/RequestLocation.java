package space.obminyashka.items_exchange.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestLocation {
    @JsonProperty("location")
    private List<RawLocation> rawLocations;
}
