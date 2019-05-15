package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FavouriteBranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavouriteBranchRepository extends JpaRepository<FavouriteBranchEntity, FavouriteBranchEntity.PK> {
}
