package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredContentToSocialMediaValidator implements
        ConstraintValidator<RequiredTextContentWhenPostingToSocialMedia, PromotionItemAddedEvent> {
    @Override
    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
        return CollectionUtils.isEmpty(value.getDestinations()) ||
                value.getDestinations().contains(Destination.NEWSLETTER) || (
                (value.getDestinations().contains(Destination.TWITTER) ||
                        value.getDestinations().contains(Destination.FB)) &&
                        !StringUtils.isEmpty(value.getContent()));
    }
}
