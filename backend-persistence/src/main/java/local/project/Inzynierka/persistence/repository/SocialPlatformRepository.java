package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.SocialMediaPlatform;

public interface SocialPlatformRepository extends ApplicationBigRepository<SocialMediaPlatform> {
    SocialMediaPlatform findBySocialMediaPlatform(String socialMediaPlatform);
}
