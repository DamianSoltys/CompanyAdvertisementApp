package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.servicelayer.dto.LoginDto;
import local.project.Inzynierka.servicelayer.dto.UserInfoDto;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;
import org.springframework.security.core.Authentication;

public interface UserAuthenticationService {
    void registerNewUser(UserRegistrationDto userRegistrationDto);

    UserInfoDto login(LoginDto userDto, Long userId, Authentication authentication);
    boolean confirmUser(String token);
}
