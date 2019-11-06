package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.servicelayer.dto.promotionitem.SendingStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;

public class AtLeast15MinutesDelayValidator implements ConstraintValidator<AtLeast15MinutesDelay, PromotionItemAddedEvent> {

    private static final long SECONDS_IN_15_MINUTES = 900;

    @Override
    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
        return !SendingStrategy.DELAYED.equals(value.getSendingStrategy()) ||
                (SendingStrategy.DELAYED.equals(value.getSendingStrategy()) &&  !startIn15Minutes(value.getStartTime()));
    }

    private boolean startIn15Minutes(Instant value) {
        return value != null && Instant.now().plusSeconds(SECONDS_IN_15_MINUTES).isAfter(value);
    }
}
