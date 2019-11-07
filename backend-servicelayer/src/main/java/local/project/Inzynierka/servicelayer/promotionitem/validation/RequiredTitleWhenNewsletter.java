package local.project.Inzynierka.servicelayer.promotionitem.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = RequiredTitleValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RequiredTitleWhenNewsletter {
    String message() default "Newsletter e-mails should have title.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
