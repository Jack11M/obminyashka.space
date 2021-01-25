package com.hillel.items_exchange.validator;

import com.hillel.items_exchange.annotation.FieldMatch;
import org.springframework.beans.BeanWrapperImpl;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, Object> {

    private String firstFieldName;
    private String secondFieldName;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstFieldName = constraintAnnotation.first();
        secondFieldName = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        boolean valid;

        Object firstObject = new BeanWrapperImpl(value).getPropertyValue(firstFieldName);
        Object secondObject = new BeanWrapperImpl(value).getPropertyValue(secondFieldName);
        if (firstObject != null) {
            valid = firstObject.equals(secondObject);
        }
        else {
            valid = secondObject == null;
        }

        return valid;
    }
}
