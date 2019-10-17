package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Voivoideship;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoivodeshipRepository extends ApplicationSmallRepository<Voivoideship> {
    Optional<Voivoideship> findByName(String name);
}
