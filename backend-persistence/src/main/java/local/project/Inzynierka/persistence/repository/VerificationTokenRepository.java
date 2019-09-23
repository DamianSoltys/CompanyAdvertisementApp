package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.VerificationToken;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends ApplicationBigRepository<VerificationToken> {

    VerificationToken findByToken(String token);
}
