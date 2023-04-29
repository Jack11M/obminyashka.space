package space.obminyashka.items_exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;
import space.obminyashka.items_exchange.util.PatternHandler;

import jakarta.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PhoneDto {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "381234567890")
    @Pattern(regexp = PatternHandler.PHONE_NUMBER,
            message = ResponseMessagesHandler.ValidationMessage.INVALID_PHONE_NUMBER)
    private String phoneNumber;

    private boolean defaultPhone;
}