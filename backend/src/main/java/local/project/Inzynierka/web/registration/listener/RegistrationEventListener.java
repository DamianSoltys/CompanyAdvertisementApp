package local.project.Inzynierka.web.registration.listener;

import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.web.registration.event.OnRegistrationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationEventListener {

    Logger logger = LoggerFactory.getLogger(RegistrationEventListener.class);

    @Autowired
    private JavaMailSender javaMailSender;


    @Async
    @EventListener
    public void handleRegistrationEvent(OnRegistrationEvent event) {
        logger.info("INSIDE EVENT HANDLER");
        logger.info(String.valueOf(event.getUser()));
        logger.info(event.getAppUrl());

        final User user = event.getUser();
        final String token = UUID.randomUUID().toString();

        /*
         * TODO Complete the implementation
         * */
    }
}
