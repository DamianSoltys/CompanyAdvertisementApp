package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.Company;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.NewsletterSubscription;
import local.project.Inzynierka.persistence.entity.VerificationToken;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.persistence.repository.NewsletterSubscriptionRepository;
import local.project.Inzynierka.persistence.repository.VerificationTokenRepository;
import local.project.Inzynierka.shared.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Service
@Slf4j
public class NewsletterServiceImpl implements NewsletterService {

    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailRepository emailRepository;

    private final CompanyRepository companyRepository;

    public NewsletterServiceImpl(NewsletterSubscriptionRepository newsletterSubscriptionRepository, VerificationTokenRepository verificationTokenRepository, EmailRepository emailRepository, CompanyRepository companyRepository) {
        this.newsletterSubscriptionRepository = newsletterSubscriptionRepository;
        this.verificationTokenRepository = verificationTokenRepository;
        this.emailRepository = emailRepository;
        this.companyRepository = companyRepository;
    }

    @Override @Transactional
    public void signUpForNewsletter(EmailAddress emailAddress, Company company) {
        NewsletterSubscription newsletterSubscription = new NewsletterSubscription();

        Timestamp now = DateUtils.getNowTimestamp();
        newsletterSubscription.setCreatedAt(now);
        newsletterSubscription.setModifiedAt(now);

        Company foundCompany = companyRepository.getByCompanyId(company.getId());
        newsletterSubscription.setCompany(foundCompany);
        EmailAddress foundEmail = emailRepository.findByEmail(emailAddress.getEmail());
        if( foundEmail == null ){
            foundEmail = new EmailAddress(emailAddress.getEmail());
            foundEmail.setCreatedAt(now);
            foundEmail.setId(0L);

            foundEmail = emailRepository.save(foundEmail);
        }
        newsletterSubscription.setEmailAddressEntity(foundEmail);

        newsletterSubscription.setId(0L);
        newsletterSubscription.setVerified(false);

        final String verificationUUID = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken(verificationUUID);
        verificationToken.setCreatedAt(now);
        verificationToken.setId(0L);
        verificationToken = verificationTokenRepository.save(verificationToken);

        newsletterSubscription.setVerificationToken(verificationToken);

        final String unsubscribeUUI = UUID.randomUUID().toString();
        VerificationToken unsubscribeToken = new VerificationToken(unsubscribeUUI);
        unsubscribeToken.setCreatedAt(now);
        unsubscribeToken.setId(0L);
        unsubscribeToken = verificationTokenRepository.save(unsubscribeToken);

        newsletterSubscription.setUnsubscribeToken(unsubscribeToken);

        newsletterSubscriptionRepository.save(newsletterSubscription);
    }
}
