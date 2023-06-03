package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.*;
import space.obminyashka.items_exchange.model.enums.Gender;

import java.time.LocalDate;

import static space.obminyashka.items_exchange.util.ResponseMessagesHandler.ValidationMessage.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ChildDto {
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    private Gender sex;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "yyyy-MM-dd")
    @NotNull(message = "{" + INVALID_NOT_NULL + "}")
    @PastOrPresent(message = "{" + INVALID_PAST_PRESENT_DATE + "}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @SuppressWarnings("unused") // Used in validation process by Spring Validator
    @AssertTrue(message = "{" + INVALID_CHILD_AGE + "}")
    private boolean isChildAgeIsValid() {
        return LocalDate.now().compareTo(birthDate) < 18;
    }
}
