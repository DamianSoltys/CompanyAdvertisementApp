package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Branch;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BranchRepository extends ApplicationBigRepository<Branch> {

    @Query("SELECT b.id from Branch b where b.company.id = ?1")
    List<Long> getAllByCompanyId(Long id);
}
