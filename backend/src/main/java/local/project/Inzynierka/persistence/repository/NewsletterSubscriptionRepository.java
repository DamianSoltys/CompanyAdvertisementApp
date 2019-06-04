package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import local.project.Inzynierka.persistence.entity.VerificationToken;

public interface NewsletterSubscriptionRepository extends ApplicationBigRepository<NewsletterSubscription> {

    NewsletterSubscription findByVerificationToken(VerificationToken verificationToken);

    NewsletterSubscription findByUnsubscribeToken(VerificationToken verificationToken);
}
