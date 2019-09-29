package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.persistence.repository.NewsletterSubscriptionRepository;
import local.project.Inzynierka.persistence.repository.VerificationTokenRepository;
import local.project.Inzynierka.servicelayer.dto.SubscriptionToCreateDto;
import local.project.Inzynierka.servicelayer.newsletter.event.NewsletterSignUpEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NewsletterService {

    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailRepository emailRepository;

    private final CompanyRepository companyRepository;

    private final ApplicationEventPublisher applicationEventPublisher;

    public NewsletterService(NewsletterSubscriptionRepository newsletterSubscriptionRepository, VerificationTokenRepository verificationTokenRepository, EmailRepository emailRepository, CompanyRepository companyRepository, ApplicationEventPublisher applicationEventPublisher) {
        this.newsletterSubscriptionRepository = newsletterSubscriptionRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailRepository = emailRepository;
        this.companyRepository = companyRepository;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Transactional
    public void signUpForNewsletter(SubscriptionToCreateDto subscriptionToCreateDto, String originHeader) {

        Company company = this.companyRepository.findById(subscriptionToCreateDto.getId()).get();

        NewsletterSubscription newsletterSubscription = NewsletterSubscription.builder()
                .company(company)
                .emailAddressEntity(getPersistedEmailAddress(subscriptionToCreateDto.getEmailToSignUp()))
                .verified(subscriptionToCreateDto.isVerified())
                .build();

        newsletterSubscription = newsletterSubscriptionRepository.save(newsletterSubscription);

        applicationEventPublisher.publishEvent(
                new NewsletterSignUpEvent(newsletterSubscription, originHeader, subscriptionToCreateDto.isVerified()));
    }

    private EmailAddress getPersistedEmailAddress(String emailAddress) {
        EmailAddress foundEmail = emailRepository.findByEmail(emailAddress);
        if (foundEmail == null) {
            foundEmail = new EmailAddress(emailAddress);

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
