package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FavouriteBranch;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FavouriteBranchRepository extends PagingAndSortingRepository<FavouriteBranch, FavouriteBranch.PK> {
}
