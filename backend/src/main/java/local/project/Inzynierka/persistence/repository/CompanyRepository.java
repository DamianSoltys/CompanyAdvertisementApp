package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyRepository extends ApplicationBigRepository<Company> {

    @Query(value = "SELECT c FROM Branch b JOIN b.company c WHERE c.id = :id ")
    Company getByCompanyId(Long id);

    List<Company> findByRegisterer(NaturalPerson registerer);
}
