package space.obminyashka.items_exchange.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageDto {
    private UUID id;
    @NotNull(message = "{invalid.not-null}")
    private byte[] resource;
}
