package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Company;
import org.springframework.data.jpa.repository.Query;

public interface CompanyRepository extends ApplicationBigRepository<Company> {

    @Query(value = "SELECT c FROM Branch b JOIN b.company c WHERE c.id = :id ")
    Company getByCompanyId(Long id);
}
