package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.shared.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Override
    public EmailAddress findByEmail(EmailAddress email) {

        EmailAddress emailAddressEntity = emailRepository.findByEmail(email.getEmail());
        if(emailAddressEntity == null ){
            return null;
        }
        return emailAddressEntity;
    }

    @Override
    public EmailAddress saveEmailAddress(EmailAddress emailAddress) {
        Timestamp now = DateUtils.getNowTimestamp();
        emailAddress.setCreatedAt(now);

        EmailAddress emailAddressEntity = emailRepository.save(emailAddress);

        return emailAddressEntity;
    }
}
