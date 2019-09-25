package local.project.Inzynierka.servicelayer.rating.listener;

import local.project.Inzynierka.servicelayer.rating.CommentService;
import local.project.Inzynierka.servicelayer.rating.event.CommentCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentEditedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CommentsEventListener {

    private final CommentService commentService;

    public CommentsEventListener(CommentService commentService) {this.commentService = commentService;}

    @Async
    @EventListener
    public void handleCommentCreation(CommentCreatedEvent event) {
        this.commentService.createComment(event);
    }

    @Async
    @EventListener
    public void handleCommentEdition(CommentEditedEvent event) {
        this.commentService.editComment(event);
    }

    @Async
    @EventListener
    public void handleCommentDeletion(CommentDeletedEvent event) {
        this.commentService.deleteComment(event);
    }
}
