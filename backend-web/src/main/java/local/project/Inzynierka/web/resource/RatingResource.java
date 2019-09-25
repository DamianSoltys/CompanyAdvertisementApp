package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.CreateRatingDto;
import local.project.Inzynierka.servicelayer.dto.EditRatingDto;
import local.project.Inzynierka.servicelayer.rating.event.CommentDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.RatingEditedEvent;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class RatingResource {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuthFacade authFacade;

    public RatingResource(ApplicationEventPublisher applicationEventPublisher, AuthFacade authFacade) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.authFacade = authFacade;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/rating")
    public void createRating(@RequestBody final CreateRatingDto createRatingDto) {

        UserAccount userAccount = this.authFacade.getAuthenticatedUser();

        RatingCreatedEvent ratingCreatedEvent = RatingCreatedEvent.builder()
                .branchId(createRatingDto.getBranchId())
                .userName(userAccount.getLoginName())
                .rating(createRatingDto.getRating())
                .build();
        applicationEventPublisher.publishEvent(ratingCreatedEvent);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/rating/{id}")
    public void editRating(@RequestBody final EditRatingDto editRatingDto, @PathVariable(value = "id") Long ratingId) {

        RatingEditedEvent ratingEditedEvent = RatingEditedEvent.builder()
                .rating(editRatingDto.getRating())
                .ratingId(ratingId)
                .build();
        applicationEventPublisher.publishEvent(ratingEditedEvent);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/rating/{id}")
    public void deleteRating(@PathVariable(value = "id") Long ratingId) {

        applicationEventPublisher.publishEvent(new CommentDeletedEvent(ratingId));
    }
}
