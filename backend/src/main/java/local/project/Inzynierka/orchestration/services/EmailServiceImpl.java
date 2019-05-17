package local.project.Inzynierka.orchestration.services;

import local.project.Inzynierka.domain.model.EmailAddress;
import local.project.Inzynierka.orchestration.mapper.EmailMapper;
import local.project.Inzynierka.persistence.entity.EmailAddressEntity;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.shared.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private EmailRepository emailRepository;

    @Autowired
    private EmailMapper emailMapper;

    @Override
    public EmailAddress findByEmail(EmailAddress email) {

        EmailAddressEntity emailAddressEntity = emailRepository.findByEmail(email.getEmail());
        if(emailAddressEntity == null ){
            return null;
        }
        return emailMapper.map(emailAddressEntity);
    }

    @Override
    public EmailAddress saveEmailAddress(EmailAddress emailAddress) {
        Timestamp now = DateUtils.getNowTimestamp();
        emailAddress.setCreatedAt(now);

        EmailAddressEntity emailAddressEntity = emailRepository.save(emailMapper.map(emailAddress));

        return emailMapper.map(emailAddressEntity);
    }
}
