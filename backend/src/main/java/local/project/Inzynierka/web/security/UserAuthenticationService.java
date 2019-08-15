package local.project.Inzynierka.web.security;

import local.project.Inzynierka.servicelayer.dto.LoginDto;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;

public interface UserAuthenticationService {
    void registerNewUser(UserRegistrationDto userRegistrationDto);

    Long login(LoginDto user);
    boolean confirmUser(String token);
}
