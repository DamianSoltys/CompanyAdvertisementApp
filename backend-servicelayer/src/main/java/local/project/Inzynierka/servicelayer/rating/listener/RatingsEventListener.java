package local.project.Inzynierka.servicelayer.rating.listener;

import local.project.Inzynierka.servicelayer.rating.RatingService;
import local.project.Inzynierka.servicelayer.rating.event.RatingCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingEditedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class RatingsEventListener {

    private final RatingService ratingService;

    public RatingsEventListener(RatingService ratingService) {this.ratingService = ratingService;}

    @Async
    @EventListener
    public void handleRatingCreation(RatingCreatedEvent ratingCreatedEvent) {
        ratingService.createRating(ratingCreatedEvent);
    }

    @Async
    @EventListener
    public void handleRatingDeletion(RatingDeletedEvent ratingDeletedEvent) {
        ratingService.deleteRating(ratingDeletedEvent);
    }

    @Async
    @EventListener
    public void handleRatingEdition(RatingEditedEvent ratingEditedEvent) {
        ratingService.editRating(ratingEditedEvent);
    }
}
