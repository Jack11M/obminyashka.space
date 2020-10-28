package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hillel.items_exchange.model.ChildGender;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ChildDto {

    @PositiveOrZero(message = "{invalid.id}")
    private long id;
    @NotNull(message = "{invalid.not-null}")
    private ChildGender sex;
    @ApiModelProperty(example = "yyyy-MM-dd")
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}