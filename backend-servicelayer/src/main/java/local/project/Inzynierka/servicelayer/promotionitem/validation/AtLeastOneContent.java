package local.project.Inzynierka.servicelayer.promotionitem.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = AtLeastOneContentValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AtLeastOneContent {
    String message() default "Promotion item should have at least one content.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
