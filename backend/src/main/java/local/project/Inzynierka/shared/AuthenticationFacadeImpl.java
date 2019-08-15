package local.project.Inzynierka.shared;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFacadeImpl implements AuthenticationFacade {

    private final UserRepository userRepository;

    public AuthenticationFacadeImpl(UserRepository userRepository) {this.userRepository = userRepository;}

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @Override
    public User getAuthenticatedUser() {
        return this.userRepository.getByAddressEmail(this.getAuthentication().getName());
    }

}
