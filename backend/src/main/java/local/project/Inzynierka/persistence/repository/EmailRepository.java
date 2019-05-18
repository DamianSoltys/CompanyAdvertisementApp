package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.EmailAddressEntity;

public interface EmailRepository extends ApplicationBigRepository<EmailAddressEntity> {
    EmailAddressEntity findByEmail(String email);
}
