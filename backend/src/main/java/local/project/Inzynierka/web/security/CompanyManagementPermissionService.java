package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyManagementPermissionService {

    private final AuthenticationFacade authenticationFacade;

    private final UserRepository userRepository;

    @Autowired
    public CompanyManagementPermissionService(AuthenticationFacade authenticationFacade, UserRepository userRepository) {
        this.authenticationFacade = authenticationFacade;
        this.userRepository = userRepository;
    }

    public boolean hasRegisteringAuthority(){
        User user = userRepository.getByAddressEmail(authenticationFacade.getAuthentication().getName());

        return user.getNaturalPerson() != null;
    }
}
