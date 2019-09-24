package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Comment;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends ApplicationBigRepository<Comment> {

    @Modifying
    @Query(value = "UPDATE comments c SET c.comment = :comment WHERE c.id = :commentId", nativeQuery = true)
    void updateComment(Long commentId, String comment);
}
