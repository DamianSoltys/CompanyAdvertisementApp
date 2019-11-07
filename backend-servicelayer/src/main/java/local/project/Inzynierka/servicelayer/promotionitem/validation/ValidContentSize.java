package local.project.Inzynierka.servicelayer.promotionitem.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ContentSizeValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ValidContentSize {
    String message() default "Promotion item contents should have length of below 16777215 Bytes.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
