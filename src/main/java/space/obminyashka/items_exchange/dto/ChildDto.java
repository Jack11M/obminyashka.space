package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import space.obminyashka.items_exchange.annotation.ValidChildAge;
import space.obminyashka.items_exchange.annotation.Zero;
import space.obminyashka.items_exchange.mapper.transfer.Exist;
import space.obminyashka.items_exchange.mapper.transfer.New;
import space.obminyashka.items_exchange.model.enums.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ChildDto {

    @Positive(groups = Exist.class, message = "{invalid.exist.id}")
    @Zero(groups = New.class, message = "{invalid.new.entity.id}")
    private long id;
    @NotNull(message = "{invalid.not-null}")
    private Gender sex;
    @ValidChildAge(message = "{invalid.child.age}")
    @ApiModelProperty(required = true, example = "yyyy-MM-dd")
    @NotNull(message = "{invalid.not-null}")
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
