package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserWithoutChildrenDto {

    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Size(min = 2, max = 50, message = "{invalid.size}")
    private String username;
    @ApiModelProperty(required = true)
    @NotEmpty(message = "{invalid.not-empty}")
    @Email(message = "{invalid.email}")
    private String email;
    @NotNull(message = "{invalid.not-null}")
    private Boolean online;
    @ApiModelProperty(value = "Empty or 2-50 symbols", example = "Mariana, Мар'яна, Марьяна")
    @NotNull(message = "{invalid.not-null}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{invalid.first-or-last.name}")
    private String firstName;
    @ApiModelProperty(value = "Empty or 2-50 symbols", example = "Kvitka-Osnovianenko, Квітка-Основ'яненко, Квитка-Основьяненко")
    @NotNull(message = "{invalid.not-null}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{invalid.first-or-last.name}")
    private String lastName;
    @NotNull(message = "{invalid.not-null}")
    private byte[] avatarImage;
    @ApiModelProperty(required = true, example = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "{invalid.not-null}")
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineTime;
    @NotNull(message = "{invalid.not-null}")
    private Set<@Valid PhoneDto> phones;
    @NotNull(message = "{invalid.not-null}")
    private Status status;
    @NotNull(message = "{invalid.not-null}")
    private LocalDateTime updated;

}
