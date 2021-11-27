package space.obminyashka.items_exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateDto {

    @ApiModelProperty(value = "Empty or 2-50 symbols", example = "Mariana, Мар'яна, Марьяна")
    @NotNull(message = "{invalid.not-null}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{invalid.first-or-last.name}")
    private String firstName;
    @ApiModelProperty(value = "Empty or 2-50 symbols",
            example = "Kvitka-Osnovianenko, Квітка-Основ'яненко, Квитка-Основьяненко")
    @NotNull(message = "{invalid.not-null}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{invalid.first-or-last.name}")
    private String lastName;
    @Size(max = 3, message = "{invalid.phones-amount}")
    @NotNull(message = "{invalid.not-null}")
    private Set<@Valid PhoneDto> phones;
}
