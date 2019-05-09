package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.EmailAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EmailRepository extends JpaRepository<EmailAddressEntity, Long> {
    EmailAddressEntity findByEmail(String email);
}
