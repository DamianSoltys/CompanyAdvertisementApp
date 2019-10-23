package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Branch;
import local.project.Inzynierka.persistence.entity.NaturalPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends ApplicationBigRepository<Branch> {

    @Query("SELECT b.id from Branch b where b.company.id = ?1")
    List<Long> getAllByCompanyId(Long id);

    Page<Branch> findAll(Pageable pageable);

    List<Branch> findByRegisterer(NaturalPerson naturalPerson);

    Optional<Branch> findByBranchUUID(String branchUUID);
}
