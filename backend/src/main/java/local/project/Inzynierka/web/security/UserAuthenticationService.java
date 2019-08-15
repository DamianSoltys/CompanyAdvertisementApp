package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;

public interface UserAuthenticationService {
    void registerNewUser(UserRegistrationDto userRegistrationDto);

    Long login(User user);
    boolean confirmUser(String token);
}
