package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.servicelayer.services.UserFacade;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationsUserDetailService implements UserDetailsService {

    private final UserFacade userFacade;

    public ApplicationsUserDetailService(UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*
        Logging in using email instead of name.
         TODO Add logging in using also name option.
        **/

        User user = userFacade.findByEmailAddress(email);
        if(user == null ){
            throw new NullPointerException();
        }

        return new UserPrincipal(user);
    }

}
