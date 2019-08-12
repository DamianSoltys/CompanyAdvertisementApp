package local.project.Inzynierka.web.controller;

import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.servicelayer.dto.LoginDto;
import local.project.Inzynierka.servicelayer.dto.UserRegistrationDto;
import local.project.Inzynierka.servicelayer.services.UserService;
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

    private final UserAuthenticationService authenticationService;

    private final UserDtoMapper mapper;

    private final UserService userService;

    private final ApplicationEventPublisher eventPublisher;

    public AuthenticationController(UserAuthenticationService authenticationService, UserDtoMapper mapper, UserService userService, ApplicationEventPublisher eventPublisher) {
        this.authenticationService = authenticationService;
        this.mapper = mapper;
        this.userService = userService;
        this.eventPublisher = eventPublisher;
    }

    @RequestMapping(value = "/auth/registration", method = RequestMethod.POST)
    public ResponseEntity registerNewUser(@RequestBody final UserRegistrationDto userRegistrationDto,
                                          final HttpServletRequest request) {

        User user = mapper.map(userRegistrationDto);
        try {
            authenticationService.registerNewUser(user);
            user = userService.findByEmailAddress(user.getEmailAddressEntity());
            eventPublisher.publishEvent(new OnRegistrationEvent(user, request.getHeader("Origin")));

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

    @RequestMapping(value = "/auth/logout", method = RequestMethod.POST)
    public String logout() {
        return "LOGGED OUT";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth/registration/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam(name = "token") String token ) {
        if( authenticationService.confirmUser(token)) {
            return ResponseEntity.ok().body("{\"data\":\"Twoje konto zostało potwiedzone\"}");
        }
        return ResponseEntity.ok().body("{\"data\":\"Nieprawidłowy token\"}");
    }

}
