package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends ApplicationBigRepository<EmailAddress> {
    EmailAddress findByEmail(String email);
}
