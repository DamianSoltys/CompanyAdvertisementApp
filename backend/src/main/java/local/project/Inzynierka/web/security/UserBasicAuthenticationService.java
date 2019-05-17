package local.project.Inzynierka.web.security;

import local.project.Inzynierka.domain.model.EmailAddress;
import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.orchestration.services.EmailService;
import local.project.Inzynierka.orchestration.services.UserService;
import local.project.Inzynierka.web.errors.BadLoginDataException;
import local.project.Inzynierka.web.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.web.errors.UserAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class UserBasicAuthenticationService implements UserAuthenticationService {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override @Transactional
    public void registerNewUser(User user)  {

        User requestedUser = userService.findByName(user.getName());
        EmailAddress requestedEmail = emailService.findByEmail(user.getEmailAddress());

        if(requestedUser != null ){
            throw new UserAlreadyExistsException();
        }
        if( requestedEmail != null) {
            throw new EmailAlreadyTakenException();
        }

        requestedEmail = new EmailAddress(user.getEmailAddress().getEmail());
        requestedEmail = this.emailService.saveEmailAddress(requestedEmail);

        requestedUser = new User(user.getName(), passwordEncoder.encode(user.getPassword()), requestedEmail);
        this.userService.createNewUser(requestedUser);

    }

    @Override
    public String login(User user)  {
        try{
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    new UserPrincipal(user), user.getPassword());

            Authentication authenticatedUser = authenticationManager.authenticate(loginToken);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
        } catch (AuthenticationException  e) {
            throw new BadLoginDataException();
        }
        return "OK";
    }


}
