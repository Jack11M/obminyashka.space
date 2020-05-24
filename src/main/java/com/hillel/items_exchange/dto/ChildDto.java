package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ChildDto {
    @NotNull(message = "{invalid.not-null}")
    private String sex;
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
}
