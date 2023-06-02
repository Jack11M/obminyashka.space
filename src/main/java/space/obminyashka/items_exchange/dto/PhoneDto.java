package space.obminyashka.items_exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import space.obminyashka.items_exchange.util.PatternHandler;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PhoneDto {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "381234567890")
    @Pattern(regexp = PatternHandler.PHONE_NUMBER, message = "{" + INVALID_PHONE_NUMBER + "}")
    private String phoneNumber;

    private boolean defaultPhone;
}