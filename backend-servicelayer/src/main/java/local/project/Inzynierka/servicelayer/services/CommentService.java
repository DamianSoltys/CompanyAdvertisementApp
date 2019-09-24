package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Comment;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.CommentRepository;
import local.project.Inzynierka.servicelayer.rating.event.CommentCreatedEvent;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserFacade userFacade;

    public CommentService(CommentRepository commentRepository, UserFacade userFacade) {
        this.commentRepository = commentRepository;
        this.userFacade = userFacade;
    }

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

}
