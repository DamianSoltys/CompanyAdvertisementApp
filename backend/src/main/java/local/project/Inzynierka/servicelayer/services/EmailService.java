package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final EmailRepository emailRepository;

    public EmailService(EmailRepository emailRepository) {
        this.emailRepository = emailRepository;
    }

    public EmailAddress findByEmail(String email) {

        return emailRepository.findByEmail(email);
    }

    public EmailAddress saveEmailAddress(EmailAddress emailAddress) {

        return emailRepository.save(emailAddress);
    }
}
