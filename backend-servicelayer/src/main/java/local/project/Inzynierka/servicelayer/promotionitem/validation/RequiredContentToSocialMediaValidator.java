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
        return CollectionUtils.isEmpty(value.getDestination()) ||
                value.getDestination().contains(Destination.NEWSLETTER) || (
                (value.getDestination().contains(Destination.TWITTER) || value.getDestination().contains(Destination.FB)) &&
                        !StringUtils.isEmpty(value.getContent()));
    }
}
