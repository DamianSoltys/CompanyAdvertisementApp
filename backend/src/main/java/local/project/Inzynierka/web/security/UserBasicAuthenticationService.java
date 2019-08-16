package local.project.Inzynierka.web.security;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.servicelayer.dto.LoginDto;
import local.project.Inzynierka.servicelayer.dto.UserInfoDto;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;
import local.project.Inzynierka.servicelayer.services.EmailService;
import local.project.Inzynierka.servicelayer.services.UserFacade;
import local.project.Inzynierka.servicelayer.services.UserPersistenceService;
import local.project.Inzynierka.web.errors.BadLoginDataException;
import local.project.Inzynierka.web.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.web.errors.UserAlreadyExistsException;
import local.project.Inzynierka.web.mapper.UserDtoMapper;
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

    private final UserFacade userFacade;

    private final EmailService emailService;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final UserDtoMapper mapper;

    private final UserPersistenceService userPersistenceService;


    public UserBasicAuthenticationService(UserFacade userFacade, EmailService emailService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder, UserDtoMapper mapper, UserPersistenceService userPersistenceService) {
        this.userFacade = userFacade;
        this.emailService = emailService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.userPersistenceService = userPersistenceService;
    }

    @Override @Transactional
    public void registerNewUser(UserRegistrationDto userRegistrationDto) {

        User requestedUser = this.userFacade.findByName(mapper.map(userRegistrationDto).getName());
        EmailAddress requestedEmail = this.emailService.findByEmail(userRegistrationDto.getEmail());

        if(requestedUser != null ){
            throw new UserAlreadyExistsException();
        }
        if( requestedEmail != null) {
            throw new EmailAlreadyTakenException();
        }

        requestedEmail = new EmailAddress(userRegistrationDto.getEmail());
        requestedEmail = this.emailService.saveEmailAddress(requestedEmail);

        requestedUser = new User(userRegistrationDto.getName(), passwordEncoder.encode(userRegistrationDto.getPassword()), requestedEmail);
        this.userFacade.createNewUser(requestedUser);

    }

    @Override
    public UserInfoDto login(LoginDto loginDto) {
        try{
            User user = mapper.map(loginDto);
            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    new UserPrincipal(user), user.getPasswordHash());

            Authentication authenticatedUser = authenticationManager.authenticate(loginToken);
            SecurityContextHolder.getContext().setAuthentication(authenticatedUser);

            return this.userPersistenceService.getUserInfo(
                    ((UserPrincipal) authenticatedUser.getPrincipal())
                            .getUser()
                            .getId());

        } catch (AuthenticationException  e) {
            throw new BadLoginDataException();
        }
    }

    @Override
    public boolean confirmUser(String token) {
        return userFacade.verifyToken(token);
    }
}
