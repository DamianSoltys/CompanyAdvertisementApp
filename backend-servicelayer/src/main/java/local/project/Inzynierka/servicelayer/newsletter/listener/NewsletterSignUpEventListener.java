package local.project.Inzynierka.servicelayer.newsletter.listener;

import local.project.Inzynierka.servicelayer.newsletter.event.OnNewsletterSignUpEvent;
import local.project.Inzynierka.servicelayer.services.NewsletterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class NewsletterSignUpEventListener {

    private final JavaMailSender javaMailSender;

    private final NewsletterService newsletterService;

    @Autowired
    public NewsletterSignUpEventListener(JavaMailSender javaMailSender, NewsletterService newsletterService) {
        this.javaMailSender = javaMailSender;
        this.newsletterService = newsletterService;
    }

    @Async
    @EventListener
    public void handleNewsletterSignUp(OnNewsletterSignUpEvent event) {

        final String signUpToken = UUID.randomUUID().toString();
        final String signOutToken = UUID.randomUUID().toString();
        newsletterService.createVerificationTokens(event.getNewsletterSubscription(), signUpToken, signOutToken);

        if( !event.isVerified()) {
            final SimpleMailMessage mailMessage = constructEmailMessage(event, signUpToken, signOutToken);
            javaMailSender.send(mailMessage);
        }
    }

    private SimpleMailMessage constructEmailMessage(OnNewsletterSignUpEvent onNewsletterSignUpEvent,
                                              String signUpToken, String signOutToken) {

        String companyName = onNewsletterSignUpEvent.getNewsletterSubscription().getCompany().getName();
        String recipient = onNewsletterSignUpEvent.getNewsletterSubscription().getEmailAddressEntity().getEmail();
        String signUpLink = onNewsletterSignUpEvent.getAppUrl()+"/newsletter/signup/"+signUpToken;
        String signOutLink =  onNewsletterSignUpEvent.getAppUrl()+"/newsletter/signout/"+signOutToken;

        String message = "Właśnie zapisano cię na listę odbiorców newslettera firmy " + companyName + "."+
                "\r\nAby przekonać nas, że jesteś tym za kogo się podajesz, kliknij jeszcze w link poniżej: \r\n"+
                signUpLink+ "\r\n\r\n" + "Aby wypisać się, kliknij tu:\r\n"+signOutLink;

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("test@example.com");
        simpleMailMessage.setSubject("Potwierdzenie zapisu na listę newslettera - " + companyName);
        simpleMailMessage.setTo(recipient);
        simpleMailMessage.setText(message);

        return simpleMailMessage;
    }
}
