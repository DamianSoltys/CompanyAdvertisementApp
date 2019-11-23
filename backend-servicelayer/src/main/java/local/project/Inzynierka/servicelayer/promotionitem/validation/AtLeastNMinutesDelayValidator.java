package local.project.Inzynierka.servicelayer.promotionitem.validation;

import local.project.Inzynierka.persistence.constants.SendingStrategy;
import local.project.Inzynierka.servicelayer.promotionitem.PromotionItemAddedEvent;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Instant;

import static local.project.Inzynierka.shared.ApplicationConstants.SECONDS_IN_N_MINUTES_DELAY;

public class AtLeastNMinutesDelayValidator implements ConstraintValidator<AtLeastNMinutesDelay, PromotionItemAddedEvent> {

    @Override
    public boolean isValid(PromotionItemAddedEvent value, ConstraintValidatorContext context) {
        return !SendingStrategy.DELAYED.equals(value.getSendingStrategy()) ||
                (SendingStrategy.DELAYED.equals(value.getSendingStrategy()) &&  !startInNMinutes(value.getPlannedSendingTime()));
    }

    private boolean startInNMinutes(Instant value) {
        return value != null && Instant.now().plusSeconds(SECONDS_IN_N_MINUTES_DELAY).isAfter(value);
    }
}
