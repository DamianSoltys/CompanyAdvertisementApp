package local.project.Inzynierka.web.controller;


import local.project.Inzynierka.domain.model.User;
import local.project.Inzynierka.web.dto.LoginDto;
import local.project.Inzynierka.web.security.UserAuthenticationService;
import local.project.Inzynierka.web.dto.UserRegistrationDto;
import local.project.Inzynierka.web.mapper.UserDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @Autowired
    private UserAuthenticationService authenticationService;

    @Autowired
    private UserDtoMapper mapper;

    @RequestMapping(value = "/user/registration", method = RequestMethod.POST)
    public void registerNewUser(@RequestBody UserRegistrationDto userRegistrationDto) {

        User user = mapper.map(userRegistrationDto);
        authenticationService.registerNewUser(user);
    }

    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public boolean login(@RequestBody LoginDto loginDto) {
        User user = mapper.map(loginDto);
        return authenticationService.login(user);

    }
    @RequestMapping(value = "/user/logout", method = RequestMethod.POST)
    public String logout() {
        return "LOGGED OUT";
    }

}
