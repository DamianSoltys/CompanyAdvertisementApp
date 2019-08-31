package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NaturalPersonRepository extends ApplicationBigRepository<NaturalPerson> {

    NaturalPerson findByPhoneNo(String phoneNo);

    @Query(value = "select * from companies c where c.registerer_id = ?1", nativeQuery = true)
    List<Company> getAllRegisteredCompanies(Long id);

    @Query(value = "select c.id from companies c where c.registerer_id = ?1", nativeQuery = true)
    List<Long> getAllRegisteredCompaniesIds(Long id);
}
