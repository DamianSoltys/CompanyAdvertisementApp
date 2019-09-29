package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.servicelayer.dto.CreateCommentDto;
import local.project.Inzynierka.servicelayer.dto.EditCommentDto;
import local.project.Inzynierka.servicelayer.rating.event.CommentCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentEditedEvent;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<?> createComment(@RequestBody final CreateCommentDto createCommentDto) {

        UserAccount userAccount = this.authFacade.getAuthenticatedUser();

        CommentCreatedEvent commentCreatedEvent = CommentCreatedEvent.builder()
                .branchId(createCommentDto.getBranchId())
                .userName(userAccount.getLoginName())
                .commentContent(createCommentDto.getComment())
                .build();
        applicationEventPublisher.publishEvent(commentCreatedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @RequestMapping(method = RequestMethod.PATCH, value = "/comment/{id}")
    public ResponseEntity<?> editComment(@RequestBody final EditCommentDto editCommentDto, @PathVariable(value = "id") Long commentId) {

        if (this.authFacade.hasPrincipalHavePermissionToCommentResource(commentId)) {
            CommentEditedEvent commentEditedEvent = CommentEditedEvent.builder()
                    .commentId(commentId)
                    .newCommentContent(editCommentDto.getComment())
                    .build();
            applicationEventPublisher.publishEvent(commentEditedEvent);

            return ResponseEntity.ok(null);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/comment/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable(value = "id") Long commentId) {

        if (this.authFacade.hasPrincipalHavePermissionToCommentResource(commentId)) {
            applicationEventPublisher.publishEvent(new CommentDeletedEvent(commentId));

            return ResponseEntity.ok(null);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);

    }
}
