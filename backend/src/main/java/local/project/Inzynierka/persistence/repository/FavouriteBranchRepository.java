package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FavouriteBranchEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FavouriteBranchRepository extends PagingAndSortingRepository<FavouriteBranchEntity, FavouriteBranchEntity.PK> {
}
