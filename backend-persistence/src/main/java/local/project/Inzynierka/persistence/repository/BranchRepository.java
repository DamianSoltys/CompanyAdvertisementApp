package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends ApplicationBigRepository<Branch>, JpaSpecificationExecutor<Branch> {

    @Query("SELECT b.id from Branch b where b.company.id = ?1")
    List<Long> getAllByCompanyId(Long id);

    Page<Branch> findAll(Pageable pageable);

    List<Branch> findByRegisterer(NaturalPerson naturalPerson);

    Optional<Branch> findByBranchUUID(String branchUUID);

    List<Branch> findByCompany_Category_Name(String category);

    List<Branch> findByCompany(Company company);

    @Query("select b from Branch b " +
            "inner join b.address a" +
            " where (:city is null or a.city = :city) and (:name is null or b.name = :city)")
    List<Branch> searchForBranch(@Param("name") String name, @Param("city") String city, Pageable pageable);

}
