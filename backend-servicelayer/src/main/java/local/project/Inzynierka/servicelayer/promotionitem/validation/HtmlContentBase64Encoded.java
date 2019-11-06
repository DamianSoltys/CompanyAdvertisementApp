package local.project.Inzynierka.servicelayer.promotionitem.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StringBase64Encoded.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface HtmlContentBase64Encoded {
    String message() default "Html Content should be base64 encoded.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
