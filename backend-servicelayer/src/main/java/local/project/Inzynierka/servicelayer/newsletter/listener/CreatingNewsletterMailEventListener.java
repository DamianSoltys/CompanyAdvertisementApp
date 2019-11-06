package local.project.Inzynierka.servicelayer.newsletter.listener;

import local.project.Inzynierka.persistence.projections.CompanySendMailInfo;
import local.project.Inzynierka.persistence.projections.NewsletterSubscriptionSendEmailInfo;
import local.project.Inzynierka.persistence.repository.CompanyRepository;
import local.project.Inzynierka.persistence.repository.NewsletterSubscriptionRepository;
import local.project.Inzynierka.servicelayer.newsletter.EmailMimeType;
import local.project.Inzynierka.servicelayer.newsletter.event.CreatingNewsletterMailEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Base64;
import java.util.List;

@Component
public class CreatingNewsletterMailEventListener {

    private final JavaMailSender javaMailSender;

    private final NewsletterSubscriptionRepository newsletterSubscriptionRepository;

    private final CompanyRepository companyRepository;

    @Autowired
    public CreatingNewsletterMailEventListener(JavaMailSender javaMailSender,
                                               NewsletterSubscriptionRepository newsletterSubscriptionRepository,
                                               CompanyRepository companyRepository) {
        this.javaMailSender = javaMailSender;
        this.newsletterSubscriptionRepository = newsletterSubscriptionRepository;
        this.companyRepository = companyRepository;
    }

    @Async
    @EventListener
    public void handleSendingNewsletterOut(CreatingNewsletterMailEvent event) throws MessagingException {
        CompanySendMailInfo company = companyRepository.getSendEmailInfoById(event.getCompanyId()).
                orElseThrow(IllegalArgumentException::new);

        List<NewsletterSubscriptionSendEmailInfo> newsletterSubscriptions = newsletterSubscriptionRepository
                .getSendEmailInfoByCompany_IdAndVerifiedTrue(company.getCompanyId());

        for (var subscription : newsletterSubscriptions) {
            javaMailSender.send(constructEmailMessage(event, subscription, company));
        }

    }


    private MimeMessage constructEmailMessage(CreatingNewsletterMailEvent creatingNewsletterMailEvent,
                                              NewsletterSubscriptionSendEmailInfo newsletterSubscription,
                                              CompanySendMailInfo company) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        String text = constructEmailText(creatingNewsletterMailEvent, newsletterSubscription);
        helper.setText(text,
                       EmailMimeType.HTML.equals(creatingNewsletterMailEvent.getEmailMimeType()));

        String companyName = company.getName();
        String recipient = newsletterSubscription.getSignedUpEmail();

        helper.setFrom("test@example.com");
        helper.setSubject(creatingNewsletterMailEvent.getSubject() + " Newsletter -" + companyName);
        helper.setTo(recipient);

        return mimeMessage;
    }

    private String constructEmailText(CreatingNewsletterMailEvent creatingNewsletterMailEvent,
                                      NewsletterSubscriptionSendEmailInfo newsletterSubscription) {
        if (EmailMimeType.HTML.equals(creatingNewsletterMailEvent.getEmailMimeType())) {
            return new String(Base64.getDecoder().decode(creatingNewsletterMailEvent.getMessage()));
        }

        String signOutLink = creatingNewsletterMailEvent.getAppUrl() + "/newsletter/signout/" +
                newsletterSubscription.getUnsubscribeToken();

        return creatingNewsletterMailEvent.getMessage() + "\r\n\r\n" +
                "\r\n\r\n" + "Aby wypisać się z listy newslettera, kliknij tu:\r\n" + signOutLink;
    }
}
