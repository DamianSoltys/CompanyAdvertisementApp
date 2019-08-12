package local.project.Inzynierka.web.mapper;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.servicelayer.dto.LoginDto;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;

@Component
public class UserDtoMapper {

    public User map(UserRegistrationDto userRegistrationDto) {
        if( userRegistrationDto == null ){
            return null;
        }
        User user = new User();
        user.setName(userRegistrationDto.getName());
        user.setPasswordHash(userRegistrationDto.getPassword());

        EmailAddress emailAddress = new EmailAddress(userRegistrationDto.getEmail());
        user.setEmailAddressEntity(emailAddress);

        return user;
    }

    public User map(LoginDto loginDto) {
        if( loginDto == null) {
            return null;
        }
        User user = new User();
        user.setPasswordHash(loginDto.getPassword());

        EmailAddress emailAddress = new EmailAddress(loginDto.getEmail());
        user.setEmailAddressEntity(emailAddress);

        return user;
    }
}
