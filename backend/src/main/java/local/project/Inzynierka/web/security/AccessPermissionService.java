package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.AuthenticationFacade;
import org.springframework.stereotype.Service;

@Service
public class AccessPermissionService {

    private final UserRepository userRepository;

    private final AuthenticationFacade authenticationFacade;

    public AccessPermissionService(UserRepository userRepository, AuthenticationFacade authenticationFacade) {
        this.userRepository = userRepository;
        this.authenticationFacade = authenticationFacade;
    }

    public boolean hasPrincipalHavePermissionToUserResource(Long userId) {
        User authenticatedUser = this.userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());
        User requestedUser = this.userRepository.findById(userId).orElse(new User());

        return authenticatedUser != null && authenticatedUser.equals(requestedUser);
    }

}
