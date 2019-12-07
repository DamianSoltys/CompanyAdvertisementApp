package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FavouriteBranch;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavouriteBranchRepository extends PagingAndSortingRepository<FavouriteBranch, Long> {

    Optional<FavouriteBranch> findByBranch_IdAndUser_Id(Long branchId, Long userId);

    List<FavouriteBranch> findByUser_Id(Long userId);

    Optional<FavouriteBranch> findByUuid(String uuid);
}
