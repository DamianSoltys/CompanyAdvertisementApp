package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.SocialProfile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SocialProfileRepository extends ApplicationBigRepository<SocialProfile> {

    List<SocialProfile> findByCompany_Id(Long companyId);

    SocialProfile findByCompany_IdAndAndSocialMediaPlatform_SocialMediaPlatform(Long companyId,
                                                                                String socialMediaPlatform);
}
