package local.project.Inzynierka.servicelayer.services;

import local.project.Inzynierka.persistence.entity.EmailAddress;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.persistence.repository.EmailRepository;
import local.project.Inzynierka.servicelayer.dto.mapper.UserDtoMapper;
import local.project.Inzynierka.servicelayer.dto.user.LoginDto;
import local.project.Inzynierka.servicelayer.dto.user.UserInfoDto;
import local.project.Inzynierka.servicelayer.dto.user.UserRegistrationDto;
import local.project.Inzynierka.shared.errors.BadLoginDataException;
import local.project.Inzynierka.shared.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.shared.errors.UserAlreadyExistsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserBasicAuthenticationService implements UserAuthenticationService {

    private final UserFacade userFacade;

    private final EmailRepository emailRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserDtoMapper mapper;

    private final UserPersistenceService userPersistenceService;


    public UserBasicAuthenticationService(UserFacade userFacade, EmailRepository emailRepository, PasswordEncoder passwordEncoder, UserDtoMapper mapper, UserPersistenceService userPersistenceService) {
        this.userFacade = userFacade;
        this.emailRepository = emailRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
        this.userPersistenceService = userPersistenceService;
    }

    @Override
    @Transactional
    public void registerNewUser(UserRegistrationDto userRegistrationDto) {

        User user = this.userFacade.findByName(mapper.map(userRegistrationDto).getName());
        if (user != null) {
            throw new UserAlreadyExistsException();
        }
        EmailAddress emailAddress = this.emailRepository.findByEmail(userRegistrationDto.getName());
        if (emailAddress != null) {
            throw new EmailAlreadyTakenException();
        }

        this.userFacade.createNewUser(constructNewUser(userRegistrationDto));

    }

    private User constructNewUser(UserRegistrationDto userRegistrationDto) {
        return new User(userRegistrationDto.getName(),
                        passwordEncoder.encode(userRegistrationDto.getPassword()),
                        this.emailRepository.save(new EmailAddress(userRegistrationDto.getEmail())));
    }

    @Override
    public UserInfoDto login(LoginDto loginDto, Authentication authentication) {
        try {
            SecurityContextHolder.getContext().setAuthentication(authentication);

            return this.userPersistenceService.getUserInfo(this.userPersistenceService.getUserIdByEmail(loginDto.getEmail()));

        } catch (AuthenticationException e) {
            throw new BadLoginDataException();
        }
    }

    @Override
    public boolean confirmUser(String token) {
        return userFacade.verifyToken(token);
    }
}
