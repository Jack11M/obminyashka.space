package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ChildAddDto {
    @NotNull(message = "{invalid.not-null}")
    private String sex;
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
