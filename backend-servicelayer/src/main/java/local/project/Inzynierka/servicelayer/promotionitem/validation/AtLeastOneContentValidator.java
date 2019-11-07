package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtLeastOneContentValidator implements ConstraintValidator<AtLeastOneContent, PromotionItemAddedEvent> {

    @Override
    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
        return !StringUtils.isEmpty(value.getContent()) || !StringUtils.isEmpty(value.getHTMLContent());
    }
}
