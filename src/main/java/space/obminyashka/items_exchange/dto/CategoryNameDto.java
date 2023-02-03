package space.obminyashka.items_exchange.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import space.obminyashka.items_exchange.util.ResponseMessagesHandler;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryNameDto {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long id;

    @ApiModelProperty(required = true)
    @NotEmpty(message = ResponseMessagesHandler.ValidationMessage.INVALID_NOT_EMPTY)
    @Size(min = 3, max = 50, message = "{" + ResponseMessagesHandler.ValidationMessage.INVALID_SIZE + "}")
    private String name;
}
