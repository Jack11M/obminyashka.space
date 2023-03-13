package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import space.obminyashka.items_exchange.model.enums.Status;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;
import space.obminyashka.items_exchange.util.PatternHandler;
import lombok.*;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY)
    @Size(min = 2, max = 50, message = ResponseMessagesHandler.ValidationMessage.INVALID_SIZE)
    private String username;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY)
    @Email(message = ResponseMessagesHandler.ValidationMessage.INVALID_EMAIL)
    private String email;
    private boolean online;
    private boolean oauth2Login;
    @Schema(description = "Empty or 2-50 symbols", example = "Mariana, Мар'яна, Марьяна")
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50,
            message = ResponseMessagesHandler.ValidationMessage.INVALID_FIRST_LAST_NAME)
    private String firstName;
    @Schema(description = "Empty or 2-50 symbols",
            example = "Kvitka-Osnovianenko, Квітка-Основ'яненко, Квитка-Основьяненко")
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @Pattern(regexp = PatternHandler.WORD_EMPTY_OR_MIN_2_MAX_50,
            message = ResponseMessagesHandler.ValidationMessage.INVALID_FIRST_LAST_NAME)
    private String lastName;
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private byte[] avatarImage;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    @PastOrPresent(message = ResponseMessagesHandler.ValidationMessage.INVALID_PAST_PRESENT_DATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastOnlineTime;
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private List<@Valid ChildDto> children;
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private Set<@Valid PhoneDto> phones;
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private Status status;
    @NotNull(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_NULL)
    private LocalDateTime updated;
}
