package local.project.Inzynierka.orchestration.mapper;

import local.project.Inzynierka.domain.model.EmailAddress;
import local.project.Inzynierka.orchestration.errors.MappingException;
import local.project.Inzynierka.persistence.entity.EmailAddressEntity;
import org.springframework.stereotype.Component;

@Component
public class EmailMapper {

    public EmailAddress map(EmailAddressEntity emailAddressEntity) {
        if( emailAddressEntity == null ) {
            throw new MappingException();
        }
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setEmail(emailAddressEntity.getEmail());
        emailAddress.setId(emailAddressEntity.getId());
        emailAddress.setCreatedAt(emailAddressEntity.getCreatedAt());
        return emailAddress;

    }

    public EmailAddressEntity map(EmailAddress emailAddress) {
        if(emailAddress == null ) {
            throw new MappingException();
        }
        EmailAddressEntity emailAddressEntity = new EmailAddressEntity();
        emailAddressEntity.setEmail(emailAddress.getEmail());
        emailAddressEntity.setId(emailAddress.getId());

        emailAddressEntity.setCreatedAt(emailAddress.getCreatedAt());

        return emailAddressEntity;
    }

}
