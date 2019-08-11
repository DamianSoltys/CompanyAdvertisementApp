package local.project.Inzynierka.web.security;

import local.project.Inzynierka.orchestration.services.EmailService;
import local.project.Inzynierka.orchestration.services.UserService;
import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.web.errors.BadLoginDataException;
import local.project.Inzynierka.web.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.web.errors.UserAlreadyExistsException;
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

    private final UserService userService;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public UserBasicAuthenticationService(UserService userService, EmailService emailService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override @Transactional
    public void registerNewUser(User user)  {

        User requestedUser = userService.findByName(user.getName());
        EmailAddress requestedEmail = emailService.findByEmail(user.getEmailAddressEntity());

        if(requestedUser != null ){
            throw new UserAlreadyExistsException();
        }
        if( requestedEmail != null) {
            throw new EmailAlreadyTakenException();
        }

        requestedEmail = new EmailAddress(user.getEmailAddressEntity().getEmail());
        requestedEmail = this.emailService.saveEmailAddress(requestedEmail);

        requestedUser = new User(user.getName(), passwordEncoder.encode(user.getPasswordHash()), requestedEmail);
        this.userService.createNewUser(requestedUser);

    }

    @Override
    public Long login(User user) {
        try{
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    new UserPrincipal(user), user.getPasswordHash());

            Authentication authenticatedUser = authenticationManager.authenticate(loginToken);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

            return ((UserPrincipal) authenticatedUser.getPrincipal()).getUser().getId();
        } catch (AuthenticationException  e) {
            throw new BadLoginDataException();
        }
    }

    @Override
    public boolean confirmUser(String token) {
        return userService.verifyToken(token);
    }
}
