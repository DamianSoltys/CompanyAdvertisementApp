package local.project.Inzynierka.persistence.repository;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import local.project.Inzynierka.persistence.entity.VerificationToken;

import java.util.List;

public interface NewsletterSubscriptionRepository extends ApplicationBigRepository<NewsletterSubscription> {

    NewsletterSubscription findByVerificationToken(VerificationToken verificationToken);

    NewsletterSubscription findByUnsubscribeToken(VerificationToken verificationToken);

    List<NewsletterSubscription> findByCompanyAndVerified(Company company, boolean verified);
}
