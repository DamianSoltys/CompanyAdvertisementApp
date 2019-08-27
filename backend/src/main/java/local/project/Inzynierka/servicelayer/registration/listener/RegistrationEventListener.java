package local.project.Inzynierka.servicelayer.registration.listener;

import local.project.Inzynierka.servicelayer.services.UserFacade;
import local.project.Inzynierka.servicelayer.registration.event.OnRegistrationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationEventListener {

    private final JavaMailSender javaMailSender;

    private final UserFacade userFacade;

    public RegistrationEventListener(JavaMailSender javaMailSender, UserFacade userFacade) {
        this.javaMailSender = javaMailSender;
        this.userFacade = userFacade;
    }

    @Async
    @EventListener
    public void handleRegistrationEvent(OnRegistrationEvent event) {

        final String userEmail = event.getUserEmail();
        final String token = UUID.randomUUID().toString();

        userFacade.createVerificationTokenForUser(userEmail, token);

        final SimpleMailMessage mailMessage = constructEmailMessage(event, userEmail, token);
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage constructEmailMessage(OnRegistrationEvent event, String userEmail, String token) {

        final String subject = "Potwierdzenie rejestracji";
        final String confirmationUrl = event.getAppUrl() + "/registerConfirm/" + token;
        final String message = "Cześć, dziękujemy za założenie konta.\r\n" +
                "Kliknij teraz w link, aby potwierdzić rejestrację ";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(userEmail);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom("test@example.com");
        return email;
    }
}
