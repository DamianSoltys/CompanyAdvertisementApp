package local.project.Inzynierka.web.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AuthenticationController {

    @Autowired
    private UserAuthenticationService authenticationService;

    @Autowired
    private UserDtoMapper mapper;

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
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

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){

        User user = mapper.map(loginDto);
        try {
            authenticationService.login(user);
            return ResponseEntity.ok().body("{\"data\":\"OK\"}");
        } catch (BadLoginDataException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }

    }
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public String logout() {
        return "LOGGED OUT";
    }

}
