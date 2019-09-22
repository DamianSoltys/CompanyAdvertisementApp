package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends ApplicationBigRepository<User> {

    User findByName(String name);

    @Query("SELECT u FROM User u INNER JOIN u.emailAddressEntity e WHERE e.email = :email")
    User getByAddressEmail(@Param("email") String email);

    User findByVerificationToken(VerificationToken verificationToken);

    @Query(value = "select c.id from comments c where c.user_id = ?1", nativeQuery = true)
    List<Long> getCommentsOfUser(Long userId);

    @Query(value = "select r.user_id from ratings r where r.user_id = ?1", nativeQuery = true)
    List<Long> getRatingsOfUser(Long userId);

    @Query(value = "select s.id from newsletter_subscriptions s where s.id_email = " +
            "(select u.id_email_address from users u where u.user_id = ?1)", nativeQuery = true)
    List<Long> getSubscriptionsOfUser(Long userId);
}
