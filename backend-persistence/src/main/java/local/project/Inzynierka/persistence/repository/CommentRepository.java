package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends ApplicationBigRepository<Comment> {
}
