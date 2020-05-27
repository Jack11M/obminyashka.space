package com.hillel.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ChildDto {
    @NotNull(message = "{invalid.not-null}")
    private String sex;
    @NotNull(message = "{invalid.not-null}")
    @Size(min=3, max = 50, message = "{invalid.size}")
    private String name;
    @PastOrPresent(message = "{invalid.past-or-present.date}")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
}
