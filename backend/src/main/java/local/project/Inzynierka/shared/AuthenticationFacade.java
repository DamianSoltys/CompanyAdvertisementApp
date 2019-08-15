package local.project.Inzynierka.shared;

import local.project.Inzynierka.persistence.entity.User;
import org.springframework.security.core.Authentication;

public interface AuthenticationFacade {
    Authentication getAuthentication();

    User getAuthenticatedUser();
}
