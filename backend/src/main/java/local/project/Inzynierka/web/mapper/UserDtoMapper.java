package local.project.Inzynierka.web.mapper;

import local.project.Inzynierka.domain.model.EmailAddress;
import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.web.dto.LoginDto;
import local.project.Inzynierka.web.dto.UserRegistrationDto;
import org.springframework.stereotype.Component;


@Component
public class UserDtoMapper {

    public User map(UserRegistrationDto userRegistrationDto) {
        if( userRegistrationDto == null ){
            return null;
        }
        User user = new User();
        user.setName(userRegistrationDto.getName());
        user.setPassword(userRegistrationDto.getPassword());

        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setEmail(userRegistrationDto.getEmail());
        user.setEmailAddress(emailAddress);

        return user;
    }

    public User map(LoginDto loginDto) {
        if( loginDto == null) {
            return null;
        }
        User user = new User();
        user.setPassword(loginDto.getPassword());

        EmailAddress emailAddress = new EmailAddress();
        emailAddress.setEmail(loginDto.getEmail());
        user.setEmailAddress(emailAddress);

        return user;
    }
}
