package local.project.Inzynierka.shared;

import org.apache.commons.validator.routines.UrlValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WebProtocolValidator implements ConstraintValidator<ValidWebProtocolUrl, String> {
    public void initialize(ValidWebProtocolUrl constraint) {
    }

    public boolean isValid(String obj, ConstraintValidatorContext context) {
        String[] schemes = {"https", "http"};
        UrlValidator urlValidator = new UrlValidator(schemes);
        return obj == null || urlValidator.isValid(obj);
    }
}
