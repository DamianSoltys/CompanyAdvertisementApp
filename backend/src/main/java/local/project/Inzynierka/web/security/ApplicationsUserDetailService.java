package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.servicelayer.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationsUserDetailService implements UserDetailsService {

    private final UserService userService;

    public ApplicationsUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*
        Logging in using email instead of name.
         TODO Add logging in using also name option.
        **/

        User user = userService.findByEmailAddress(email);
        if(user == null ){
            throw new NullPointerException();
        }

        return new UserPrincipal(user);
    }

}
