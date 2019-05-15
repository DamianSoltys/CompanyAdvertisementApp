package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.RatingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatingRepository  extends JpaRepository<RatingEntity, Long> {
}
