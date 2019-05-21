package local.project.Inzynierka.web.registration.listener;

import local.project.Inzynierka.orchestration.services.UserService;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.web.registration.event.OnRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationEventListener {

    Logger logger = LoggerFactory.getLogger(RegistrationEventListener.class);

    /*@Autowired
    private JavaMailSender javaMailSender;
    */

    @Autowired
    private UserService userService;

    @Async
    @EventListener
    public void handleRegistrationEvent(OnRegistrationEvent event) {
        logger.info("INSIDE EVENT HANDLER");
        logger.info(String.valueOf(event.getUser()));
        logger.info(event.getAppUrl());

        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();

        userService.createVerificationTokenForUser(user,token);

        final SimpleMailMessage mailMessage = constructEmailMessage(event, user, token);
        logger.info(String.valueOf(mailMessage));
    }

    private SimpleMailMessage constructEmailMessage(OnRegistrationEvent event, User user, String token) {

        final String recipientAddress = user.getEmailAddressEntity().getEmail();
        final String subject = "Potwierdzenie rejestracji";
        final String confirmationUrl = event.getAppUrl() + "/user/registrationConfirm?token=" + token;
        final String message = "Kliknij w link, aby potwierdzić rejestrację ";
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message + " \r\n" + confirmationUrl);
        email.setFrom("example@example.com");
        return email;
    }
}
