package local.project.Inzynierka.servicelayer.rating;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Comment;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.CommentRepository;
import local.project.Inzynierka.servicelayer.dto.rating.CommentGetDto;
import local.project.Inzynierka.servicelayer.rating.event.CommentCreatedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentDeletedEvent;
import local.project.Inzynierka.servicelayer.rating.event.CommentEditedEvent;
import local.project.Inzynierka.servicelayer.services.UserFacade;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final UserFacade userFacade;

    public CommentService(CommentRepository commentRepository, UserFacade userFacade) {
        this.commentRepository = commentRepository;
        this.userFacade = userFacade;
    }

    @Transactional
    public Long createComment(CommentCreatedEvent event) {
        User user = this.userFacade.findByName(event.getUserName());
        Branch branch = Branch.builder().id(event.getBranchId()).build();

        Comment comment = Comment.builder()
                .branch(branch)
                .user(user)
                .comment(event.getCommentContent())
                .build();

        return Optional.of(this.commentRepository.save(comment))
                .map(Comment::getId)
                .get();
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

    public Optional<CommentGetDto> getComment(UserAccount requestingUser, Long commentId) {
        return this.commentRepository.findById(commentId)
                .map(comment -> this.buildCommentGetDto(comment, requestingUser));
    }

    public Page<CommentGetDto> getCommentsByUser(UserAccount requestingUser, Long userId, Pageable pageable) {
        return this.commentRepository
                .findAllByUser(User.builder().id(userId).build(), pageable)
                .map(comment -> this.buildCommentGetDto(comment, requestingUser));
    }

    public Page<CommentGetDto> getCommentsByBranchAndUser(UserAccount requestingUser, Long branchId, Long userId, Pageable pageable) {
        return this.commentRepository
                .findAllByBranchAndUser(Branch.builder().id(branchId).build(),
                                        User.builder().id(userId).build(),
                                        pageable)
                .map(comment -> this.buildCommentGetDto(comment, requestingUser));
    }

    public Page<CommentGetDto> getCommentsByBranch(UserAccount requestingUser, Long branchId, Pageable pageable) {
        return this.commentRepository
                .findAllByBranch(Branch.builder().id(branchId).build(),
                                 pageable)
                .map(comment -> this.buildCommentGetDto(comment, requestingUser));
    }

    public Page<CommentGetDto> getComments(UserAccount requestingUser, Pageable pageable) {
        return this.commentRepository
                .findAll(pageable)
                .map(comment -> this.buildCommentGetDto(comment, requestingUser));
    }

    private CommentGetDto buildCommentGetDto(Comment comment, UserAccount userAccount) {
        Boolean isOwnBranchCommented = userAccount != null && (userAccount.isNaturalPersonRegistered() &&
                requestingUserIsOwnerOfCommentedBranch(comment, userAccount));

        return CommentGetDto.builder()
                .isOwnBranchCommented(isOwnBranchCommented)
                .branchId(comment.getBranch().getId())
                .commentId(comment.getId())
                .username(comment.getUser().getName())
                .userId(comment.getUser().getId())
                .comment(comment.getComment())
                .build();
    }

    private boolean requestingUserIsOwnerOfCommentedBranch(Comment comment, UserAccount userAccount) {
        return userAccount.personId().equals(comment.getBranch().getCompany().getRegisterer().getId());
    }
}

