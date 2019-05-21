package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends ApplicationBigRepository<User> {

    User findByName(String name);

    @Query("SELECT u FROM User u INNER JOIN u.emailAddressEntity e WHERE e.email = :email")
    User getByAddressEmail(@Param("email") String email);
}
