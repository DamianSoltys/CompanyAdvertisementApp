package local.project.Inzynierka.servicelayer.rating;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Rating;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.RatingRepository;
import local.project.Inzynierka.servicelayer.dto.rating.RatingGetDto;
import local.project.Inzynierka.servicelayer.rating.event.RatingCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingEditedEvent;
import local.project.Inzynierka.servicelayer.services.UserFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final UserFacade userFacade;

    public RatingService(RatingRepository ratingRepository, UserFacade userFacade) {
        this.ratingRepository = ratingRepository;
        this.userFacade = userFacade;
    }

    @Transactional
    public Long createRating(RatingCreatedEvent ratingCreatedEvent) {

        User user = this.userFacade.findByName(ratingCreatedEvent.getUserName());
        Rating rating = buildRating(ratingCreatedEvent, user);

        return Optional.of(this.ratingRepository.save(rating)).map(Rating::getId).get();
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

    public Page<RatingGetDto> getRatingsByBranchAndUser(Long branchId, Long userId, Pageable pageable) {
        return this.ratingRepository
                .findAllByBranchAndUser(Branch.builder().id(branchId).build(),
                                        User.builder().id(userId).build(),
                                        pageable)
                .map(this::buildRatingGetDto);
    }

    public Page<RatingGetDto> getRatingsByUser(Long userId, Pageable pageable) {
        return this.ratingRepository
                .findAllByUser(User.builder().id(userId).build(),
                               pageable)
                .map(this::buildRatingGetDto);
    }

    public Page<RatingGetDto> getRatingsByBranch(Long branchId, Pageable pageable) {
        return this.ratingRepository
                .findAllByBranch(Branch.builder().id(branchId).build(),
                                 pageable)
                .map(this::buildRatingGetDto);
    }

    public Page<RatingGetDto> getRatings(Pageable pageable) {
        return this.ratingRepository
                .findAll(pageable)
                .map(this::buildRatingGetDto);
    }

    private RatingGetDto buildRatingGetDto(Rating rating) {
        return RatingGetDto.builder()
                .branchId(rating.getBranch().getId())
                .ratingId(rating.getId())
                .rating(rating.getRating())
                .userId(rating.getUser().getId())
                .build();
    }
}
