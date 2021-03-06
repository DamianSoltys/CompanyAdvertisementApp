package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.promotionitem.event.PromotionItemAddedEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredTitleValidator implements ConstraintValidator<RequiredTitleWhenNewsletter, PromotionItemAddedEvent> {
    public void initialize(RequiredTitleWhenNewsletter constraint) {
    }

    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
        return CollectionUtils.isEmpty(value.getDestinations()) || (
                !CollectionUtils.isEmpty(value.getDestinations()) &&
                        (!value.getDestinations().contains(Destination.NEWSLETTER) ||
                                (value.getDestinations().contains(Destination.NEWSLETTER) &&
                                        !StringUtils.isEmpty(value.getEmailTitle()))));
    }
}
