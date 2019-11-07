package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class AtWillStrategyValidator implements ConstraintValidator<LackOfDelayTimeWhenSendingStrategyAtWill, PromotionItemAddedEvent> {

    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {

        return !SendingStrategy.AT_WILL.equals(value.getSendingStrategy()) && (
                SendingStrategy.AT_WILL.equals(value.getSendingStrategy()) && value.getStartTime() == null);
    }
}
