package space.obminyashka.items_exchange.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import space.obminyashka.items_exchange.util.PatternHandler;

import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PhoneDto {
    @ApiModelProperty(required = true, example = "+38(123)456-78-90, 38 123 456 78 90")
    @Pattern(regexp = PatternHandler.PHONE_NUMBER, message = "{invalid.phone.number}")
    private String phoneNumber;

    private boolean defaultPhone;
}