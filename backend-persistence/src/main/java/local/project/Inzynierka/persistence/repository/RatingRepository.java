package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Rating;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingRepository extends ApplicationBigRepository<Rating> {

    @Modifying
    @Query(value = "UPDATE ratings r SET r.rating = :rating WHERE r.rating_id = :ratingId", nativeQuery = true)
    void updateRating(Long ratingId, Integer rating);
}
