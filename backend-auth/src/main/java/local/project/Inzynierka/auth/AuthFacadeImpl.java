package local.project.Inzynierka.auth;

import local.project.Inzynierka.persistence.repository.UserRepository;
import local.project.Inzynierka.shared.UserAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthFacadeImpl implements AuthFacade {

    private final UserRepository userRepository;

    public AuthFacadeImpl(UserRepository userRepository) {this.userRepository = userRepository;}

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public UserAccount getAuthenticatedUser() {
        return this.userRepository.getByAddressEmail(this.getAuthentication().getName());
    }

    @Override
    public boolean hasPrincipalHavePermissionToUserResource(Long userId) {
        return Optional.ofNullable(this.getAuthenticatedUser())
                .map(user -> user.getId().equals(userId))
                .orElse(false);
    }

    @Override
    public boolean hasPrincipalHavePermissionToNaturalPersonResource(Long userId, Long personId) {
        return Optional.ofNullable(this.getAuthenticatedUser())
                .map(user -> user.getId().equals(userId) &&
                        user.isNaturalPersonRegistered() &&
                        user.personId().equals(personId))
                .orElse(false);
    }

}
