package space.obminyashka.items_exchange.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import space.obminyashka.items_exchange.rest.regexp.PatternHandler;

import static space.obminyashka.items_exchange.rest.response.message.ResponseMessagesHandler.ValidationMessage.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PhoneDto {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "+381234567890")
    @NotNull(message = "{" + INVALID_NOT_NULL_PROPERTY + "}")
    @Pattern(regexp = PatternHandler.PHONE_NUMBER, message = "{" + INVALID_PHONE_NUMBER + "}")
    private String phoneNumber;

    private boolean defaultPhone;
}