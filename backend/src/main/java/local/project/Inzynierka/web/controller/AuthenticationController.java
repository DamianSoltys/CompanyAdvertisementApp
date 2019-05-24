package local.project.Inzynierka.web.controller;


import local.project.Inzynierka.orchestration.services.UserService;
import local.project.Inzynierka.persistence.entity.User;
import local.project.Inzynierka.web.dto.LoginDto;
import local.project.Inzynierka.web.dto.UserRegistrationDto;
import local.project.Inzynierka.web.errors.BadLoginDataException;
import local.project.Inzynierka.web.errors.EmailAlreadyTakenException;
import local.project.Inzynierka.web.errors.UserAlreadyExistsException;
import local.project.Inzynierka.web.mapper.UserDtoMapper;
import local.project.Inzynierka.web.registration.event.OnRegistrationEvent;
import local.project.Inzynierka.web.security.UserAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Slf4j
public class AuthenticationController {

    @Autowired
    private UserAuthenticationService authenticationService;

    @Autowired
    private UserDtoMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

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
        try {
            authenticationService.login(user);
            return ResponseEntity.ok().body("{\"data\":\"OK\"}");
        } catch (BadLoginDataException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }

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
