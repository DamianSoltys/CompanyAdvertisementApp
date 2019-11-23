package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.promotionitem.event.PromotionItemAddedEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ContentSizeValidator implements ConstraintValidator<ValidContentSize, PromotionItemAddedEvent> {

    private static final int MEDIUM_TEXT_MAX_SIZE = 16777215;

    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
        return (value.getHTMLContent() == null || value.getHTMLContent().length() < MEDIUM_TEXT_MAX_SIZE) &&
                (value.getContent() == null || value.getContent().length() < MEDIUM_TEXT_MAX_SIZE);
    }
}
