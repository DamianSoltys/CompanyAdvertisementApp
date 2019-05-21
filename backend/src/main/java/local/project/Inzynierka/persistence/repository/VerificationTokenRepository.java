package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.VerificationTokenEntity;

public interface VerificationTokenRepository extends ApplicationBigRepository<VerificationTokenEntity> {

    VerificationTokenEntity findByToken(String token);
}
