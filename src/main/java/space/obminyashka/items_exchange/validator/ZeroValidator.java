package space.obminyashka.items_exchange.validator;

import space.obminyashka.items_exchange.annotation.Zero;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class ZeroValidator implements ConstraintValidator<Zero, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        return value.equals(0L);
    }
}
