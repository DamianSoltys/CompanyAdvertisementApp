package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends ApplicationBigRepository<CommentEntity> {
}
