package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.SocialProfile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialProfileRepository extends ApplicationBigRepository<SocialProfile> {

    List<SocialProfile> findByCompany(Company company);
}
