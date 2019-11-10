package local.project.Inzynierka.servicelayer.promotionitem.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AtLeastNMinutesDelayValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AtLeastNMinutesDelay {
    String message() default "Sending of promotion item should be delayed for at least 15 minutes.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
