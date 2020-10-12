package com.hillel.items_exchange.validator;

import com.hillel.items_exchange.annotation.Zero;
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
