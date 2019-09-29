package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Rating;
import local.project.Inzynierka.persistence.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends ApplicationBigRepository<Rating> {

    @Modifying
    @Query(value = "UPDATE ratings r SET r.rating = :rating WHERE r.rating_id = :ratingId", nativeQuery = true)
    void updateRating(Long ratingId, Integer rating);

    Page<Rating> findAllByUser(User user, Pageable pageable);

    Page<Rating> findAllByBranch(Branch branch, Pageable pageable);

    Page<Rating> findAllByBranchAndUser(Branch branch, User user, Pageable pageable);

    Page<Rating> findAll(Pageable pageable);
}
