package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.persistence.constants.SendingStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NotDelayedStrategyValidator implements ConstraintValidator<LackOfDelayTimeWhenNotDelayedStrategy, PromotionItemAddedEvent> {

    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {

        return value.getSendingStrategy().equals(SendingStrategy.DELAYED) || (
                value.getSendingStrategy().equals(SendingStrategy.AT_WILL) &&
                        value.getPlannedSendingTime() == null) ||
                (value.getSendingStrategy().equals(SendingStrategy.AT_CREATION) &&
                        value.getPlannedSendingTime() == null);
    }
}
