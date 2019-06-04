package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NewsletterSubscription;

public interface NewsletterService {

    NewsletterSubscription signUpForNewsletter(EmailAddress emailAddress, Company company, boolean verified);

    void createVerificationTokens(NewsletterSubscription newsletterSubscription, String signUpToken,
                                  String signOutToken);
}
