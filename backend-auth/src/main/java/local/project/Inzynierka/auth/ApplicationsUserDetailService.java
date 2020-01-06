package local.project.Inzynierka.auth;

import local.project.Inzynierka.servicelayer.services.UserFacade;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationsUserDetailService implements UserDetailsService {

    private final UserFacade userFacade;

    public ApplicationsUserDetailService(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        return Optional.ofNullable(userFacade.findByEmailAddress(email))
                .map(UserPrincipal::new)
                .orElseThrow(NullPointerException::new);
    }

}
