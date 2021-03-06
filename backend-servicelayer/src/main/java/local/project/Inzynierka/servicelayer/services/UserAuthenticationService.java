package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.servicelayer.dto.user.LoginDto;
import local.project.Inzynierka.servicelayer.dto.user.UserInfoDto;
import local.project.Inzynierka.servicelayer.dto.user.UserRegistrationDto;
import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {
    void registerNewUser(UserRegistrationDto userRegistrationDto);

    UserInfoDto login(LoginDto userDto, Authentication authentication);
    boolean confirmUser(String token);
}
