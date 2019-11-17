package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FacebookSocialProfile;
import local.project.Inzynierka.persistence.entity.SocialProfile;
import org.springframework.stereotype.Repository;

@Repository
public interface FacebookSocialProfileRepository extends ApplicationBigRepository<FacebookSocialProfile> {
    FacebookSocialProfile findBySocialProfile(SocialProfile socialProfile);
}
