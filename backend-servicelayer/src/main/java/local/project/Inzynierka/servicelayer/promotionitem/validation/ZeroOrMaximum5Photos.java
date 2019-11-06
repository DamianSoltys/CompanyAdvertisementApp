package local.project.Inzynierka.servicelayer.promotionitem.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PhotosNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ZeroOrMaximum5Photos {
    String message() default "Promotion item should have 0 to 5 photos.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
