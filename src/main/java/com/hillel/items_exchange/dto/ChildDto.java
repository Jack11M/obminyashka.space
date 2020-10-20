package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hillel.items_exchange.annotation.Zero;
import com.hillel.items_exchange.mapper.transfer.Exist;
import com.hillel.items_exchange.mapper.transfer.New;
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
    private String sex;
    @ApiModelProperty(example = "yyyy-MM-dd")
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}