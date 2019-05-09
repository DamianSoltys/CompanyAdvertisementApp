package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.User;
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
    private UserAuthenticationService userAuthenticationService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        /*
        Logging in using email instead of name.
         TODO Add logging in using also name option.
        **/

        Logger logger = LoggerFactory.getLogger("loadUserByName");
        logger.info("BEFORE");
        User user = userAuthenticationService.findUserByEmail(email);

        logger.info("AFTER");



        logger.info(String.valueOf(user));

        return new UserPrincipal(user);
    }

}
