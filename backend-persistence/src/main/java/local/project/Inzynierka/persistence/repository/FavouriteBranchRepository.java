package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FavouriteBranch;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavouriteBranchRepository extends PagingAndSortingRepository<FavouriteBranch, FavouriteBranch.PK> {
}
