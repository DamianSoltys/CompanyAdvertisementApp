package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.dto.promotionitem.Destination;
import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RequiredStartTimeValidator implements ConstraintValidator<StartTimeRequiredWhenDelayed, PromotionItemAddedEvent> {

   public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
      return !SendingStrategy.DELAYED.equals(value.getSendingStrategy()) ||
              SendingStrategy.DELAYED.equals(value.getSendingStrategy()) && value.getStartTime() != null;
   }
}
