package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.NewsletterSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterSubscriptionRepository extends JpaRepository<NewsletterSubscriptionEntity, Long> {
}
