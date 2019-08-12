package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.persistence.repository.NewsletterSubscriptionRepository;
import local.project.Inzynierka.persistence.repository.VerificationTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsletterService {

    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailRepository emailRepository;

    public NewsletterService(NewsletterSubscriptionRepository newsletterSubscriptionRepository, VerificationTokenRepository verificationTokenRepository, EmailRepository emailRepository) {
        this.newsletterSubscriptionRepository = newsletterSubscriptionRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailRepository = emailRepository;
    }

    @Transactional
    public NewsletterSubscription signUpForNewsletter(EmailAddress emailAddress, Company company, boolean verified) {

        NewsletterSubscription newsletterSubscription = NewsletterSubscription.builder()
                .company(company)
                .emailAddressEntity(getPersistedEmailAddress(emailAddress))
                .id(0L)
                .verified(verified)
                .build();

        return newsletterSubscriptionRepository.save(newsletterSubscription);
    }

    private EmailAddress getPersistedEmailAddress(EmailAddress emailAddress) {
        EmailAddress foundEmail = emailRepository.findByEmail(emailAddress.getEmail());
        if (foundEmail == null) {
            foundEmail = new EmailAddress(emailAddress.getEmail());
            foundEmail.setId(0L);

            foundEmail = emailRepository.save(foundEmail);
        }
        return foundEmail;
    }

    @Transactional
    public void createVerificationTokens(NewsletterSubscription newsletterSubscription, String signUpToken, String signOutToken) {

        newsletterSubscription.setVerificationToken(this.getPersistedToken(signUpToken));
        newsletterSubscription.setUnsubscribeToken(this.getPersistedToken(signOutToken));

        newsletterSubscriptionRepository.save(newsletterSubscription);
    }

    private VerificationToken getPersistedToken(String token) {
        VerificationToken verificationToken = new VerificationToken(token);
        verificationToken.setId(0L);
        verificationToken = verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    @Transactional
    public boolean confirmEmail(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return false;
        }

        NewsletterSubscription newsletterSubscription =
                newsletterSubscriptionRepository.findByVerificationToken(verificationToken);

        if (newsletterSubscription == null) {
            return false;
        }
        newsletterSubscription.setVerified(true);
        newsletterSubscriptionRepository.save(newsletterSubscription);

        return true;
    }

    public boolean confirmSigningOut(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null) {
            return false;
        }

        NewsletterSubscription newsletterSubscription =
                newsletterSubscriptionRepository.findByUnsubscribeToken(verificationToken);

        if (newsletterSubscription == null) {
            return false;
        }
        newsletterSubscription.setVerified(false);
        newsletterSubscriptionRepository.save(newsletterSubscription);

        return true;
    }
}
