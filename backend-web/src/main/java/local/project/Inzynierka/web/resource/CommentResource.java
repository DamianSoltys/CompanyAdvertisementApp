package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.CreateCommentDto;
import local.project.Inzynierka.servicelayer.rating.event.CommentCreatedEvent;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class CommentResource {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final AuthFacade authFacade;

    public CommentResource(ApplicationEventPublisher applicationEventPublisher, AuthFacade authFacade) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.authFacade = authFacade;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/comment")
    public void createComment(@RequestBody final CreateCommentDto createCommentDto) {

        UserAccount userAccount = this.authFacade.getAuthenticatedUser();

        CommentCreatedEvent commentCreatedEvent = CommentCreatedEvent.builder()
                .branchId(createCommentDto.getBranchId())
                .userName(userAccount.getLoginName())
                .commentContent(createCommentDto.getComment())
                .build();
        applicationEventPublisher.publishEvent(commentCreatedEvent);
    }
}
