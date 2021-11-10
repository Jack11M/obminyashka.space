package space.obminyashka.items_exchange.annotation;

import space.obminyashka.items_exchange.validator.ChildAgeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ChildAgeValidator.class)
public @interface ValidChildAge {

    String message() default "Child must be below 18 years of age";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
