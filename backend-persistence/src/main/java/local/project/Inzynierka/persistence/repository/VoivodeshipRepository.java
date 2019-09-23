package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Voivoideship;
import org.springframework.stereotype.Repository;

@Repository
public interface VoivodeshipRepository extends ApplicationSmallRepository<Voivoideship> {
    Voivoideship findByName(String name);
}
