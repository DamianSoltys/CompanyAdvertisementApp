package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.shared.AuthenticationFacade;
import org.springframework.stereotype.Service;

@Service
public class AccessPermissionService {

    private final AuthenticationFacade authenticationFacade;

    public AccessPermissionService(AuthenticationFacade authenticationFacade) {
        this.authenticationFacade = authenticationFacade;
    }

    public boolean hasPrincipalHavePermissionToUserResource(Long userId) {
        User authenticatedUser = this.authenticationFacade.getAuthenticatedUser();

        return authenticatedUser != null && authenticatedUser.getId().equals(userId);
    }

    public boolean hasPrincipalHavePermissionToNaturalPersonResource(Long userId, Long personId) {
        User authenticatedUser = this.authenticationFacade.getAuthenticatedUser();

        return authenticatedUser != null &&
                authenticatedUser.getId().equals(userId) &&
                authenticatedUser.hasRegisteredNaturalPerson() &&
                authenticatedUser.getNaturalPerson().getId().equals(personId);
    }
}
