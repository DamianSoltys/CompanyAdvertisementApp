package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.rating.CreateRatingDto;
import local.project.Inzynierka.servicelayer.dto.rating.EditRatingDto;
import local.project.Inzynierka.servicelayer.dto.rating.RatingGetDto;
import local.project.Inzynierka.servicelayer.rating.RatingService;
import local.project.Inzynierka.servicelayer.rating.event.CommentDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingEditedEvent;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class RatingResource {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthFacade authFacade;
    private final RatingService ratingService;

    public RatingResource(ApplicationEventPublisher applicationEventPublisher, AuthFacade authFacade, RatingService ratingService) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.authFacade = authFacade;
        this.ratingService = ratingService;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rating")
    public ResponseEntity<?> createRating(@RequestBody final CreateRatingDto createRatingDto) {

        UserAccount userAccount = this.authFacade.getAuthenticatedUser();

        RatingCreatedEvent ratingCreatedEvent = RatingCreatedEvent.builder()
                .branchId(createRatingDto.getBranchId())
                .userName(userAccount.getLoginName())
                .rating(createRatingDto.getRating())
                .build();
        Long ratingId = this.ratingService.createRating(ratingCreatedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(ratingId);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/rating/{id}")
    public ResponseEntity<?> editRating(@RequestBody final EditRatingDto editRatingDto, @PathVariable(value = "id") Long ratingId) {

        if (this.authFacade.hasPrincipalHavePermissionToRatingResource(ratingId)) {
            RatingEditedEvent ratingEditedEvent = RatingEditedEvent.builder()
                    .rating(editRatingDto.getRating())
                    .ratingId(ratingId)
                    .build();
            applicationEventPublisher.publishEvent(ratingEditedEvent);

            return ResponseEntity.ok(null);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/rating/{id}")
    public ResponseEntity<?> deleteRating(@PathVariable(value = "id") Long ratingId) {

        if (this.authFacade.hasPrincipalHavePermissionToRatingResource(ratingId)) {
            applicationEventPublisher.publishEvent(new CommentDeletedEvent(ratingId));

            return ResponseEntity.ok(null);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/rating")
    public Page<RatingGetDto> getRatings(@RequestParam(value = "userId", required = false) Long userId,
                                         @RequestParam(value = "branchId", required = false) Long branchId,
                                         Pageable pageable) {
        Page<RatingGetDto> result;

        if (branchId != null && userId != null) {
            result = this.ratingService.getRatingsByBranchAndUser(branchId, userId, pageable);
        } else if (branchId == null && userId != null) {
            result = this.ratingService.getRatingsByUser(userId, pageable);
        } else if (branchId != null && userId == null) {
            result = this.ratingService.getRatingsByBranch(branchId, pageable);
        } else {
            result = this.ratingService.getRatings(pageable);
        }

        return result;
    }
}
