package local.project.Inzynierka.servicelayer.rating;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Rating;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.RatingRepository;
import local.project.Inzynierka.servicelayer.rating.event.RatingCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingEditedEvent;
import local.project.Inzynierka.servicelayer.services.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserFacade userFacade;

    public RatingService(RatingRepository ratingRepository, UserFacade userFacade) {
        this.ratingRepository = ratingRepository;
        this.userFacade = userFacade;
    }

    @Transactional
    public void createRating(RatingCreatedEvent ratingCreatedEvent) {

        User user = this.userFacade.findByName(ratingCreatedEvent.getUserName());
        Rating rating = buildRating(ratingCreatedEvent, user);

        this.ratingRepository.save(rating);
    }

    private Rating buildRating(RatingCreatedEvent ratingCreatedEvent, User user) {
        return Rating.builder()
                .branch(Branch.builder().id(ratingCreatedEvent.getBranchId()).build())
                .rating(ratingCreatedEvent.getRating())
                .user(user)
                .build();
    }

    @Transactional
    public void deleteRating(RatingDeletedEvent ratingDeletedEvent) {
        this.ratingRepository.delete(Rating.builder().id(ratingDeletedEvent.getRatingId()).build());
    }

    @Transactional
    public void editRating(RatingEditedEvent ratingEditedEvent) {
        this.ratingRepository.updateRating(ratingEditedEvent.getRatingId(), ratingEditedEvent.getRating());
    }
}
