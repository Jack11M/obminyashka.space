package space.obminyashka.items_exchange.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import space.obminyashka.items_exchange.util.PatternHandler;

import java.util.Set;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    @Schema(description = "Empty or 2-50 symbols", example = "Mariana, Мар'яна, Марьяна")
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{" + INVALID_FIRST_LAST_NAME + "}")
    private String firstName;
    @Schema(description = "Empty or 2-50 symbols",
            example = "Kvitka-Osnovianenko, Квітка-Основ'яненко, Квитка-Основьяненко")
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{" + INVALID_FIRST_LAST_NAME + "}")
    private String lastName;
    @Size(max = 3, message = "{" + INVALID_PHONES_AMOUNT + "}")
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private Set<@Valid PhoneDto> phones;
}
