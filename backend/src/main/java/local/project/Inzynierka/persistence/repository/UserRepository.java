package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends ApplicationBigRepository<UserEntity> {

    UserEntity findByName(String name);

    @Query("SELECT u FROM UserEntity u INNER JOIN u.emailAddressEntity e WHERE e.email = :email")
    UserEntity getByAddressEmail(@Param("email") String email);
}
