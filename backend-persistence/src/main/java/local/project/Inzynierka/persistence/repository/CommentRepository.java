package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Comment;
import local.project.Inzynierka.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends ApplicationBigRepository<Comment> {

    @Modifying
    @Query(value = "UPDATE comments c SET c.comment = :comment WHERE c.id = :commentId", nativeQuery = true)
    void updateComment(Long commentId, String comment);

    Page<Comment> findAllByUser(User user, Pageable pageable);

    Page<Comment> findAllByBranch(Branch branch, Pageable pageable);

    Page<Comment> findAllByBranchAndUser(Branch branch, User user, Pageable pageable);

    Page<Comment> findAll(Pageable pageable);
}
