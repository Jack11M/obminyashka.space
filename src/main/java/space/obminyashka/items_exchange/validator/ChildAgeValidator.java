package space.obminyashka.items_exchange.validator;

import org.springframework.stereotype.Component;
import space.obminyashka.items_exchange.annotation.ValidChildAge;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Component
public class ChildAgeValidator implements ConstraintValidator<ValidChildAge, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate != null && LocalDate.now().compareTo(localDate) < 18;
    }
}
