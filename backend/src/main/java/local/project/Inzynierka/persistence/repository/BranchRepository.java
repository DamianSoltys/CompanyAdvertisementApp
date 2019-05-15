package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.BranchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BranchRepository extends JpaRepository<BranchEntity, Long> {

}
