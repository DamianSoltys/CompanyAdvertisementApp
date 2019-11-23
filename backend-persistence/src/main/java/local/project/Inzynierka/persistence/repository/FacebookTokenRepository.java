package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.FacebookToken;
import local.project.Inzynierka.persistence.entity.SocialProfile;

import java.util.List;

public interface FacebookTokenRepository extends ApplicationBigRepository<FacebookToken> {
    List<FacebookToken> findByFacebookSocialProfile_SocialProfile(SocialProfile socialProfile);

    FacebookToken findByFacebookSocialProfile_SocialProfileAndType(SocialProfile socialProfile, String type);
}
