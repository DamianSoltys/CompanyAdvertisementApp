package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.servicelayer.dto.LoginDto;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;
import local.project.Inzynierka.shared.utils.SimpleJsonFromStringCreator;
import local.project.Inzynierka.web.errors.BadLoginDataException;
import local.project.Inzynierka.web.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.web.errors.UserAlreadyExistsException;
import local.project.Inzynierka.web.mapper.UserDtoMapper;
import local.project.Inzynierka.web.registration.event.OnRegistrationEvent;
import local.project.Inzynierka.web.security.AuthorizationHeader;
import local.project.Inzynierka.web.security.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class AuthenticationController {

    private static final String ORIGIN_HEADER = "Origin";
    private static final String CONFIRMATION_MESSAGE = "Twoje konto zostało potwiedzone";
    private static final String WRONG_TOKEN = "Nieprawidłowy token";

    private final UserAuthenticationService authenticationService;

    private final UserDtoMapper mapper;

    private final ApplicationEventPublisher eventPublisher;

    public AuthenticationController(UserAuthenticationService authenticationService, UserDtoMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.authenticationService = authenticationService;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
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
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){

        User user = mapper.map(loginDto);
        Long userId;

        try {
            userId = authenticationService.login(user);

        } catch (BadLoginDataException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

        return ResponseEntity.ok().headers(this.createAuthorizationHTTPHeader(loginDto))
                .body(SimpleJsonFromStringCreator.toJson(userId.toString()));

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
