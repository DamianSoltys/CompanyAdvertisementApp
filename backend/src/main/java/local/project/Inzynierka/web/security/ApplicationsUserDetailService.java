package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.EmailAddress;
import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.orchestration.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationsUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*
        Logging in using email instead of name.
         TODO Add logging in using also name option.
        **/

        User user = userService.findByEmailAddress(new EmailAddress(email));
        if(user == null ){
            throw new NullPointerException();
        }

        return new UserPrincipal(user);
    }

}
