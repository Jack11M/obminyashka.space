package space.obminyashka.items_exchange.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ImageDto {
    private UUID id;
    private byte @NotNull(message = "{" + INVALID_NOT_NULL + "}") [] resource;
}
