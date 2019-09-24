package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Comment;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.CommentRepository;
import local.project.Inzynierka.servicelayer.rating.event.CommentCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentEditedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserFacade userFacade;

    public CommentService(CommentRepository commentRepository, UserFacade userFacade) {
        this.commentRepository = commentRepository;
        this.userFacade = userFacade;
    }

    @Transactional
    public void createComment(CommentCreatedEvent event) {
        User user = this.userFacade.findByName(event.getUserName());
        Branch branch = Branch.builder().id(event.getBranchId()).build();

        Comment comment = Comment.builder()
                .branch(branch)
                .user(user)
                .comment(event.getCommentContent())
                .build();

        this.commentRepository.save(comment);
    }

    @Transactional
    public void editComment(CommentEditedEvent event) {
        this.commentRepository.updateComment(event.getCommentId(), event.getNewCommentContent());
    }

    @Transactional
    public void deleteComment(CommentDeletedEvent event) {
        this.commentRepository.delete(
                Comment.builder().id(event.getCommentId()).build());
    }
}
