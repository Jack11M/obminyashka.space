package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import space.obminyashka.items_exchange.model.enums.Gender;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ChildDto {
    @NotNull(message = "{invalid.not-null}")
    private Gender sex;
    @ApiModelProperty(required = true, example = "yyyy-MM-dd")
    @NotNull(message = "{invalid.not-null}")
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @SuppressWarnings("unused") // Used in validation process by Spring Validator
    @AssertTrue(message = "{invalid.child.age}")
    private boolean isChildAgeIsValid() {
        return LocalDate.now().compareTo(birthDate) < 18;
    }
}
