package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.util.PatternHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "{" + INVALID_NOT_EMPTY + "}")
    @Size(min = 2, max = 50, message = "{" + INVALID_SIZE + "}")
    private String username;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "{" + INVALID_NOT_EMPTY + "}")
    @Email(message = "{" + INVALID_EMAIL + "}")
    private String email;
    private boolean online;
    private boolean oauth2Login;
    @Schema(description = "Empty or 2-50 symbols", example = "Mariana, Мар'яна, Марьяна")
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{" + INVALID_FIRST_LAST_NAME + "}")
    private String firstName;
    @Schema(description = "Empty or 2-50 symbols",
            example = "Kvitka-Osnovianenko, Квітка-Основ'яненко, Квитка-Основьяненко")
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50, message = "{" + INVALID_FIRST_LAST_NAME + "}")
    private String lastName;
    private byte @NotNull(message = "{" + INVALID_NOT_NULL + "}") [] avatarImage;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @PastOrPresent(message = "{" + INVALID_PAST_PRESENT_DATE + "}")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineTime;
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private List<@Valid ChildDto> children;
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private Set<@Valid PhoneDto> phones;
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private Status status;
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private LocalDateTime updated;
}
