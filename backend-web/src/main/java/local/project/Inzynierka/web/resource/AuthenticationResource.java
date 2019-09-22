package local.project.Inzynierka.web.resource;

import local.project.Inzynierka.auth.AuthFacade;
import local.project.Inzynierka.auth.AuthorizationHeader;
import local.project.Inzynierka.auth.UserPrincipal;
import local.project.Inzynierka.servicelayer.dto.LoginDto;
import local.project.Inzynierka.servicelayer.dto.UserInfoDto;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;
import local.project.Inzynierka.servicelayer.registration.event.OnRegistrationEvent;
import local.project.Inzynierka.servicelayer.services.UserAuthenticationService;
import local.project.Inzynierka.shared.UsernamePasswordAuthentication;
import local.project.Inzynierka.shared.errors.BadLoginDataException;
import local.project.Inzynierka.shared.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.shared.errors.UserAlreadyExistsException;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class AuthenticationResource {

    private static final String ORIGIN_HEADER = "Origin";
    private static final String CONFIRMATION_MESSAGE = "Twoje konto zostało potwiedzone";
    private static final String WRONG_TOKEN = "Nieprawidłowy token";

    private final UserAuthenticationService authenticationService;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthFacade authFacade;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResource(UserAuthenticationService authenticationService, ApplicationEventPublisher eventPublisher, AuthFacade authFacade, AuthenticationManager authenticationManager) {
        this.authenticationService = authenticationService;
        this.eventPublisher = eventPublisher;
        this.authFacade = authFacade;
        this.authenticationManager = authenticationManager;
    }

    @RequestMapping(value = "/auth/registration", method = RequestMethod.POST)
    public ResponseEntity registerNewUser(@RequestBody final UserRegistrationDto userRegistrationDto,
                                          final HttpServletRequest request) {

        try {
            authenticationService.registerNewUser(userRegistrationDto);
            eventPublisher.publishEvent(new OnRegistrationEvent(userRegistrationDto.getEmail(), request.getHeader(ORIGIN_HEADER)));

            return ResponseEntity.ok().body("");
        } catch (UserAlreadyExistsException | EmailAlreadyTakenException e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @RequestMapping(value = "/auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {

        UserInfoDto userInfo;

        try {
            UsernamePasswordAuthentication usernamePasswordAuthentication = new UsernamePasswordAuthentication() {
                @Override
                public String getEmail() {
                    return loginDto.getEmail();
                }

                @Override
                public String getPassword() {
                    return loginDto.getPassword();
                }
            };

            UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(
                    new UserPrincipal(usernamePasswordAuthentication), usernamePasswordAuthentication.getPassword());

            Authentication authentication = authenticationManager.authenticate(loginToken);
            userInfo = authenticationService.login(loginDto, this.authFacade.getAuthenticatedUser().getId(), authentication);

        } catch (BadLoginDataException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.ok().headers(this.createAuthorizationHTTPHeader(loginDto))
                .body(userInfo);
    }

    private HttpHeaders createAuthorizationHTTPHeader(@RequestBody LoginDto loginDto) {
        AuthorizationHeader authorizationHeader = new AuthorizationHeader(loginDto.getEmail(), loginDto.getPassword());
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, authorizationHeader.getAuthorizationHeaderValue());
        return headers;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth/registration/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam(name = "token") String token ) {
        if( authenticationService.confirmUser(token)) {
            return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(CONFIRMATION_MESSAGE));
        }
        return ResponseEntity.ok().body(SimpleJsonFromStringCreator.toJson(WRONG_TOKEN));
    }

}
