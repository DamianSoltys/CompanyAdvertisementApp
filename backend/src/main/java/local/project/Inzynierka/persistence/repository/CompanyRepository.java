package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.CompanyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<CompanyEntity, Long> {
}
